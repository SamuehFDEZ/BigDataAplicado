// Databricks notebook source
// MAGIC %md
// MAGIC # Prerequisites - Datasets
// MAGIC We need to download some datasets to do the exercises. <br>
// MAGIC Note how we can execute shell using the magic %sh, or just use scala by default... As long as that's the default in the selector above :-) <br>
// MAGIC For readability, once all this section is executed, and all the datasets are in DBFS, you can collapse the output using the [-] box next to this card

// COMMAND ----------

// MAGIC %sh 
// MAGIC for b in bank.csv vehicles.csv characters.csv netflix_titles.csv; do
// MAGIC   curl -O "https://raw.githubusercontent.com/masfworld/datahack_docker/master/zeppelin/data/${b}"
// MAGIC done

// COMMAND ----------

dbutils.fs.mkdirs("/dataset")
Seq("bank.csv", "vehicles.csv", "characters.csv", "netflix_titles.csv").foreach( b =>
    dbutils.fs.cp(s"file:/databricks/driver/$b", s"dbfs:/dataset/$b")
)

// COMMAND ----------

dbutils.fs.ls("/dataset")

// COMMAND ----------

// MAGIC %md
// MAGIC # 1 - Read Data with Spark SQL
// MAGIC
// MAGIC Spark SQL provides handy ways to read the data. In this section we'll play around with CSVs.
// MAGIC Don't freak out! We'll use RDDs for a while to learn some methods, and then we'll use the "native" SQL way, which is a lot better.

// COMMAND ----------

// Let's have a look into the CSV we want to work with.
// As you can see it's a semicolon-separated, quoted-strings, csv file
dbutils.fs.head("/dataset/bank.csv", 512)

// COMMAND ----------

// We define the RDD just like we did in the previous day!!
val bankRDD = spark.sparkContext.textFile("/dataset/bank.csv")

// And then we can parse the CSV just like we did before: Manually
val bankTransformedRDD = bankRDD
  // Split each attribute from the CSV
  .map(l => l.split(";"))
  // Remove the header of the file (Notice how this is hardly reusable)
  .filter(s => s(0) != "\"age\"")
  // Then we can create a tuple selecting the "columns" we want. Ugly
  .map(row => (row(0).toInt, row(1).replace("\"", ""), row(2).replace("\"", ""), row(3).replace("\"", ""), row(5).replace("\"", "").toLong))
  .cache

// COMMAND ----------

// MAGIC %md
// MAGIC ## 1.1 - Infer the DF from the RDD
// MAGIC
// MAGIC Reading data from RDD. Converting RDD to DataFrame implicitly inferring the schema.

// COMMAND ----------

// Let's import all the Spark SQL functions and the implicits for the Scala API
import org.apache.spark.sql.functions._
import spark.implicits._

// Now we can define a dataframe from the
val bankDF = bankTransformedRDD
  // The method "toDF" in the RDD allows you to transform an RDD directly to a Dataframe just with the column names!
  // This uses the implicits imported above
  .toDF("age", "job", "marital", "education", "balance")

// COMMAND ----------

// We can print the schema, which has been infered automatically from the type system
bankDF.printSchema()

// COMMAND ----------

// Let's group by marital, get the average balance and order by descending average balance
// Note: $"x" is an alias for col("x")!! You can use it interchangingbly. 
// Note: $"x" syntax is often used in notebooks/spark shell but less so in "real" code
val bankGroupedDF = bankDF
.groupBy($"marital")
.agg(avg(col("balance")).as("balance_avg"))
.orderBy($"balance_avg".desc)
.cache

// You can print the dataframe to the standard output using the show command (False here means 'do not truncate the columns')
// This is pure spark and you can use it everywhere. Shell, code etc...
bankGroupedDF.show(false)

// COMMAND ----------

// Display is a command **for the notebooks** so it can only be used here to display a dataframe as a properly rendered HTML table
display(bankGroupedDF)

// COMMAND ----------

// We can create a temporary view that can be queried later
bankDF.createOrReplaceTempView("bank")

// COMMAND ----------

// We can query the data from the temporary tables we created
display(spark.sql("select * from bank order by balance desc limit 10"))

// COMMAND ----------

// MAGIC %md
// MAGIC ## 1.2 - Use an Schema explicitly
// MAGIC
// MAGIC Converting the RDD to a DataFrame applying a specific schema using "createDataFrame" method.

// COMMAND ----------

// We need some extra imports when we want to define the schema manually
import org.apache.spark.sql._
import org.apache.spark.sql.types._

// A StructType is just an array of struct fields
// Note that struct fields can be struct types, or arrays, even though we don't have those things here ;-)
val bankSchema = StructType(Array(
  StructField("age", IntegerType),
  StructField("job", StringType),
  StructField("marital", StringType),
  StructField("education", StringType),
  StructField("balance", LongType)
))

// We can convert the RDD to a Dataframe with the explicit schema, rather than inference this way
// Note: When you have a fixed structure, using explicit schemas helps you avoid unexpected problems, failing fast if data is malformed
val bankExplicitDF = spark.createDataFrame(
  // You need to provide an RDD[Row] for which you can just map your RDD of tuples this way
  // This is ugly I know (But we could've done this directly instead of working with a tuple RDD)
  bankTransformedRDD.map(t => Row(t._1, t._2, t._3, t._4, t._5)), 
  // You can provide the bankSchema then
  bankSchema
)

// We can create another temp view for the DF with explicit schema
bankExplicitDF.createOrReplaceTempView("bank_explicit")

// COMMAND ----------

// The final schema is the same as before
bankExplicitDF.printSchema()

// COMMAND ----------

// We can query the data from the temporary tables we created explicitly and it should be the same
display(spark.sql("select * from bank_explicit order by balance desc limit 10"))

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #1 - Let's go native!
// MAGIC ---
// MAGIC Load file "vehicle.csv" *directlly* in a DataFrame, show the content and print the schema <br>
// MAGIC *Hint: https://spark.apache.org/docs/latest/sql-data-sources-csv.html*
// MAGIC

// COMMAND ----------

// Let's use a new dataset. Observe carefully the CSV format.
dbutils.fs.head("/dataset/vehicles.csv", 512)

// COMMAND ----------

// Now we need to read a dataframe using the native spark.read methods
// Remember to specify the separator, and tell spark this file contains a header
// Infer the schema from the file
val vehiclesDF = spark.read
  .???

// COMMAND ----------

// MAGIC %md
// MAGIC <b>Ask yourself</b>: How absurdly better is this compared with lower level RDDs?

// COMMAND ----------

// Now we can display it
display(vehiclesDF)

// COMMAND ----------

// Or print the schema, this is a DF after all
vehiclesDF.printSchema()

// COMMAND ----------

// MAGIC %md
// MAGIC **Task:** Filter previous DataFrame to get vehicles where the capacity is greater than 70<br>
// MAGIC *Hint: Mind the type of the capacity that was inferred automatically*

// COMMAND ----------

val vehiclesFilteredDF = vehiclesDF
  .???

display(vehiclesFilteredDF)


// COMMAND ----------

// MAGIC %md
// MAGIC # 2 - Aggregation functions

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #2 - Averaging
// MAGIC ---
// MAGIC From Vehicles dataframe loaded in previous exerciese, get the average of passengers  by vehicle class<br>
// MAGIC Note: Mind the types
// MAGIC

// COMMAND ----------

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

val vehiclesAvgDF = vehiclesDF
  .???

display(vehiclesAvgDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #3 - Eye colours!
// MAGIC ---
// MAGIC Load "characters.csv" file and get the most common eye color among all the characters

// COMMAND ----------

val charactersDF = spark.read
  .???


// COMMAND ----------

val charactersEyeDF = charactersDF
  .???

display(charactersEyeDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #4 - SQL Gender
// MAGIC
// MAGIC Move characters dataframe into a temp table, and get the number of characters by gender<br>
// MAGIC Let's consider gender "none" should be "NA" instead

// COMMAND ----------

charactersDF
.withColumn("gender",
    when(col("gender") === "none", lit("NA"))
        .otherwise(col("gender")))
.createOrReplaceTempView("characters")

// COMMAND ----------

// MAGIC %sql
// MAGIC select gender, count(*) as count from characters group by gender

// COMMAND ----------

// MAGIC %md
// MAGIC # 3 - User Defined Functions

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #5 - Loading Netflix
// MAGIC Load file "/dataset/netflix_titles.csv" into a DataFrame as we did before.<br>
// MAGIC Go [here](https://scholar.google.es/scholar?hl=en&as_sdt=0%2C5&as_vis=1&q=repetition+learning&btnG=) to find the why

// COMMAND ----------

dbutils.fs.head("/dataset/netflix_titles.csv", 1024)

// COMMAND ----------

// I can do this all day
val netflixDF = spark.read
  .option("inferSchema", "true")
  .option("header", "true")
  .csv("/dataset/netflix_titles.csv")

// COMMAND ----------

netflixDF.printSchema()

// COMMAND ----------

display(netflixDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #6 - Netflix time
// MAGIC ---
// MAGIC Get the year in which more movies where added.<br>
// MAGIC Filter out the TV shows.<br>
// MAGIC Use a **UDF** to get the year it was added to the catalog.<br>
// MAGIC Display *in the standard output* the top ten release years with more movies
// MAGIC

// COMMAND ----------

val netflixMoviesDF = netflixDF.???

display(netflixMoviesDF)

// COMMAND ----------

val getYearAdded = ???

netflixMoviesDF
  .???
  .show(10, false)
