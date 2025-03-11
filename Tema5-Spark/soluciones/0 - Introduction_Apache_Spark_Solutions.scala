// Databricks notebook source
// MAGIC %md
// MAGIC # Prerequisites - Datasets
// MAGIC We need to download some datasets to do the exercises. <br>
// MAGIC Note how we can execute shell using the magic %sh, or just use scala by default... As long as that's the default in the selector above :-) <br>
// MAGIC For readability, once all this section is executed, and all the datasets are in DBFS, you can collapse the output using the [-] box next to this card

// COMMAND ----------

// MAGIC %sh
// MAGIC # Let's download some sample data from
// MAGIC for b in frankenstein.txt el_quijote.txt characters.csv planets.csv; do
// MAGIC   curl -O "https://raw.githubusercontent.com/masfworld/datahack_docker/master/zeppelin/data/${b}"
// MAGIC done

// COMMAND ----------

// Create a folder to store the datasets
dbutils.fs.mkdirs("/dataset")

// Move ech dataset from the local FS of the driver into the distributed file system (DBFS)
// Further read: https://docs.databricks.com/en/dbfs/index.html
Seq("frankenstein.txt", "el_quijote.txt", "characters.csv", "planets.csv").foreach( b =>
    dbutils.fs.cp(s"file:/databricks/driver/$b", s"dbfs:/dataset/$b")
)

// COMMAND ----------

// Let's display what's in DBFS right now
dbutils.fs.ls("/dataset").foreach(println)

// COMMAND ----------



// COMMAND ----------

// MAGIC %md
// MAGIC # 1 - Basic RDD reads
// MAGIC Text file RDDs can be created using SparkContext’s textFile method. This method takes a URI for the file (either a local path on the machine, or a hdfs://, s3a://, etc URI) and reads it as a collection of lines. Some more info -> [here](https://spark.apache.org/docs/latest/rdd-programming-guide.html#basics) <-

// COMMAND ----------

// Use the spark context to read a text file as RDD[String] (You can always check the type of the result in the output of the notebook!)
val frankRDD = spark.sparkContext.textFile("/dataset/frankenstein.txt")

// COMMAND ----------

// From now on, frankRDD is an abstraction over the frankenstein file, contained in the DFS underneath.
// It's fault tolerant and it can be operated in parallel.
println(s"Partitions: ${frankRDD.partitions.size}")
println(s"First line: ${frankRDD.first}")
println(s"Number of lines: ${frankRDD.count}")

// COMMAND ----------

// MAGIC %md
// MAGIC <b>ASK YOURSELF:</b> Has the file been processed twice from scratch? Uhm...

// COMMAND ----------

// MAGIC %md
// MAGIC # 2 - Parallelize RDDs & basic actions
// MAGIC Spark allows you to parallelize existing collections. That means, taking a collection which exists only in the driver, partition it and push it into the executors (When it's the right moment). More information -> [here](https://spark.apache.org/docs/latest/rdd-programming-guide.html#parallelized-collections) <-

// COMMAND ----------

// Spark will take this small collection and parallelize it
val someNumbers = Seq(25, 20, 15, 10, 5)
val tinyRDD = spark.sparkContext.parallelize(someNumbers)
println(s"Partitions: ${tinyRDD.partitions.size}")
tinyRDD.reduce((a, b) => a + b)

// COMMAND ----------

// MAGIC %md
// MAGIC <b>ASK YOURSELF:</b> What type is "tinyRDD"?

// COMMAND ----------

// Spark will choose the "best" partitioning by default, but sometimes you may want to choose the number of partitions yourself
val tinyRDDin5chunks = spark.sparkContext.parallelize(someNumbers, 5) // So there's a 5 there
println(s"Partitions: ${tinyRDDin5chunks.partitions.size}")
tinyRDDin5chunks.reduce(_ + _) // <-- Get used to these anonymous parameters in scala, this is equivalent to (a, b) => a + b

// COMMAND ----------

// MAGIC %md
// MAGIC # 3 - Basic transformations
// MAGIC ---
// MAGIC So far we have seen [actions](https://spark.apache.org/docs/latest/rdd-programming-guide.html#actions) like 'first' or 'count' but we haven't seen [transformations](https://spark.apache.org/docs/latest/rdd-programming-guide.html#transformations) yet.<br>
// MAGIC All transformations in Spark are lazy, in that they do not compute their results right away. Instead, they just remember the transformations applied to some base dataset (e.g. a file). The transformations are only computed when an action requires a result to be returned to the driver program.<br>
// MAGIC We will see two crucual transformations now:
// MAGIC 1. <b>Map</b> --> It applies a function to each element to the RDD, keeping the size of the RDD, but not necessarily the type (Here Spark is acting as a [Functor](https://nikgrozev.com/2016/03/14/functional-programming-and-category-theory-part-1-categories-and-functors/) in case you want to go into the rabbit hole)
// MAGIC 2. <b>ReduceByKey</b> --> It applies reduce to the values for each key of the Key/Value pair. The end result will be an RDD with one distinct key, and the reduced values.
// MAGIC 3. <b>SortByKey</b> --> It applies sorting over the K/V pairs (For which K must implement Ordered)

// COMMAND ----------

// Let's read Frankie again
val frankRDD = spark.sparkContext.textFile("/dataset/frankenstein.txt")
println(s"Count before map: ${frankRDD.count()}")

// In this case we generate an RDD mapping with f(String -> Tuple2[String, Int])
val tupleRDD = frankRDD.map(l => (l, 1)) // Equivalent to map((_,1))
println(s"Count after map: ${tupleRDD.count()}")

// Reduce by key will apply the reduce function for each distinct key
val countsRDD = tupleRDD.reduceByKey((a, b) => a + b) // Equivalent to reduceByKey(_ + _)

// The count contains only the tuples with distinct keys and aggregated counts
println(s"Count after reduce: ${countsRDD.count()}")

// We can take an arbitrary number of elements from the RDD (Or just .collect() them all to the driver)
println(s"===== 10 lines ======")
countsRDD.take(10).foreach(println) // .collect() would just take ALL of the elements (Mind the implications for large datasets)

// COMMAND ----------

// MAGIC %md
// MAGIC <b>ASK YOURSELF:</b> How many distinct lines we had in the text? How many times did Spark open this file again?? Uhm...

// COMMAND ----------

// We can get the data sorted by key
val sortedRDD = countsRDD.sortByKey()

// Let's take all of them! YOLO!!
// collect() aggregates all the data from the executors into an array that must fit into the available memory in the JVM
val allLines = sortedRDD.collect()

println("====== 10 tuples ======")
// We can take in scala too (allLines is not an RDD, it's an Array!)
allLines.take(10).foreach(println)

// Notice how Spark and Scala collections are very similar? This is pretty neat.
println(s"Theoretically this should be the count of lines in the file again: ${allLines.map(_._2).reduce(_+_)}")
println(s"You'll also see the output will dump a pretty big Array below this line...")

// COMMAND ----------

// MAGIC %md
// MAGIC # 4 - Filter
// MAGIC ---
// MAGIC Filter is another very core transformation you can apply to the data. E.g. You may only want lines which contain one specific word
// MAGIC

// COMMAND ----------

// Let's define this again (Even though it's in the context already, probably)
val frankRDD = spark.sparkContext.textFile("/dataset/frankenstein.txt")

// Let's get all the lines which contain the word "the"
val frankWithoutTheRDD = frankRDD.filter(line => line.contains("the"))
frankWithoutTheRDD.count()

// COMMAND ----------

// Now if we invert the condition...
val frankWithoutTheRDD = frankRDD.filter(line => !line.contains("the"))
frankWithoutTheRDD.count()

// COMMAND ----------

// MAGIC %md
// MAGIC <b>ASK YOURSELF:</b> What the...?
// MAGIC
// MAGIC Hehe just kidding. No, really. Can you <b>imagine the possibilities</b> of just basic map/filter/reduce operations alone?

// COMMAND ----------

// MAGIC %md
// MAGIC # Hands on #1 - Wordcounting Frankie
// MAGIC ---
// MAGIC - Get the <b>word</b>count for the file "frankenstein.txt" splitting the text by space
// MAGIC - Just take words longer than 4 characters
// MAGIC - We don't care about the commas and dots in the words, remove them
// MAGIC - Sort by the most frequent words first
// MAGIC - Get the top ten only and pretty print the results (Don't just go around dumping ugly tuples ugh)
// MAGIC - Be concise!
// MAGIC
// MAGIC <i>TIP: If there only was a way to <b>map</b> A[String] -> A[A[String]] and <b>flatten</b> A[A[String]] -> A[String] in one go...!</i>
// MAGIC

// COMMAND ----------
//Se carga el archivo frankenstein.txt en un RDD (Resilient Distributed Dataset).
//Cada línea del archivo es un elemento del RDD.
val frankieAgainRDD = spark.sparkContext.textFile("/dataset/frankenstein.txt")
frankieAgainRDD
  // We just split every word of every line and get an RDD[String] in one go
  // Note: map would produce instead an RDD[T[String]], and it's pretty nice how spark 
  // can work with the Scala collections to know that T is iterable and RDD[T[_]] can be flattened to just RDD[_]
  //Divide cada línea en palabras usando el espacio " " como separador.
  // Se usa .flatMap() porque cada línea se convierte en múltiples palabras, y queremos un solo RDD con todas ellas.
  .flatMap(_.split(" "))
  // Let's take words longer than 4 chars
  .filter(_.length > 4)
  
  // Now we do the K/1 trick and remove the unwanted chars in one go

  // Se eliminan comas , y puntos ..
  // Se asigna a cada palabra un valor 1, generando pares clave-valor del tipo (palabra, 1).
  .map(l => (l.replace(",","").replace(".",""), 1))
  
  //Agrupa por palabra y suma los valores 1, obteniendo el número de veces que aparece cada palabra.
  // Resultado: (palabra, frecuencia), por ejemplo: ("monster", 23).
  .reduceByKey(_ + _)
  
  // Because we want to sort by count, let's invert the K/V!
  // Se intercambian los valores para que el conteo sea la clave y la palabra el valor.
  //Resultado: (frecuencia, palabra), por ejemplo: (23, "monster").
  .map(t => (t._2, t._1))
  // We sort descending, take 10 and print them
  
  //Se ordenan los pares (frecuencia, palabra) en orden descendente (false).
  .sortByKey(false)
  //Se toman solo las 10 palabras más frecuentes.
  .take(10) 
  //se imprime contador -> palabra
  .foreach(t => println(s"${t._1} -> '${t._2}'"))

// COMMAND ----------

// MAGIC %md
// MAGIC <b>JUST CURIOUS...</b> Can you get the longest 10 words in the book?
// MAGIC

// COMMAND ----------

frankieAgainRDD
  .flatMap(_.split(" "))
  .map(w => (w.length, w.replace(",","").replace(".","")))
  .sortByKey(false)
  .take(10)
  .map(_._2)
  .foreach(println)

// COMMAND ----------

// MAGIC %md
// MAGIC <i>Ok maybe we should've splitted by the special dash character too...</i> <b> ¯\\_(ツ)_/¯ </b>
// MAGIC

// COMMAND ----------

// MAGIC %md
// MAGIC # 5 - CSV files with RDDs
// MAGIC ---
// MAGIC While spark provides [some fancy ways](https://spark.apache.org/docs/latest/sql-data-sources-csv.html) to deal with CSV, that's not in scope of the RDD basics today!<br>
// MAGIC That doesn't mean we can't just load CSV files and work with them, does it?<br>
// MAGIC It's a bit archaic but it's fun too

// COMMAND ----------

// Let's open a pair of CSV files, which are just text after all!
val charactersRDD = spark.sparkContext.textFile("/dataset/characters.csv").map(_.split(",").toList).cache
val planetsRDD = spark.sparkContext.textFile("/dataset/planets.csv").map(_.split(";").toList).cache

// Printing these is not as fancy as printing Dataframes (More tomorrow) but you get the idea
println("========= CHARACTERS ===========")
charactersRDD.take(10).foreach(l => println(l.toString))

println("========= PLANETS ===========")
planetsRDD.take(10).foreach(println)

// COMMAND ----------

// In case you are wondering, yes, we can ignore the header in RDDs, though it's a bit clumsy
val charactersNoHeaderRDD = charactersRDD.mapPartitionsWithIndex(
    (idx, it) => if (idx == 0) it.drop(1) else it)
val planetsNoHeaderRDD = planetsRDD.mapPartitionsWithIndex(
    (idx, it) => if (idx == 0) it.drop(1) else it)

// COMMAND ----------

println("========= CHARACTERS ===========")
charactersNoHeaderRDD.take(10).foreach(println)

println("========= PLANETS ===========")
planetsNoHeaderRDD.take(10).foreach(println)

// COMMAND ----------

// MAGIC %md
// MAGIC # Hands on #2 - Joining the datasets
// MAGIC ---
// MAGIC The assignment is easy to describe: Using the RDDs from the previous step... <b>For each star wars character, get the population of their homeworld</b>
// MAGIC
// MAGIC You will apparently need to <b>join</b>:
// MAGIC - 'homeworld' and 'name' from the characters RDD
// MAGIC - 'name' and 'population' from the planets RDD
// MAGIC - Join on the characters 'homeworld' vs planet 'name'
// MAGIC
// MAGIC <i>TIP: There is a transformation in spark for which when called on RDDs of type (K, V) and (K, W), returns a dataset of (K, (V, W)) pairs with all pairs of elements for each key. Its name starts with 'jo' and ends with 'in'.</i>
// MAGIC
// MAGIC Can you map the results into a <b>provided scala case class</b> and collect them? (That is, RDD[Result] -> List[Result])

// COMMAND ----------

// This is a very specific result type!
case class Result(character: String, planet: String, population: String)

// COMMAND ----------

// Extract the right 'columns' from the RDDs (This serves as an introduction of why Spark SQL is a lot more popular than RDDs)
val charactersKvRDD = charactersNoHeaderRDD.map(x => (x(8), x(0)))
val planetsKvRDD = planetsNoHeaderRDD.map(x => (x(0), x(8)))

// Join the data and create a List[Result]
val results = charactersKvRDD.join(planetsKvRDD)

//Transforma cada tupla en un objeto de tipo Result, que representa la información de salida con:
//Nombre del personaje.
//ID del planeta.
//Población del planeta.
.map{ case (planet, (character, population)) => Result(character, planet, population) }

//Convierte el RDD en una lista en memoria.
.collect()

//Ordena la lista por el nombre del personaje.
.sortBy(_.character)

//Print the results
//Itera sobre la lista y muestra los resultados en la consola en un formato legible.
results.foreach(r => println(s"${r.character} => From ${r.planet}, Pop. ${r.population}"))
