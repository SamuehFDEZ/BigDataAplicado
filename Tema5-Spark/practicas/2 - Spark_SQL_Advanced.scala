// Databricks notebook source
// MAGIC %md
// MAGIC # Prerequisites - Datasets
// MAGIC We need to download some datasets to do the exercises. <br>
// MAGIC Note how we can execute shell using the magic %sh, or just use scala by default... As long as that's the default in the selector above :-) <br>
// MAGIC For readability, once all this section is executed, and all the datasets are in DBFS, you can collapse the output using the [-] box next to this card

// COMMAND ----------

// MAGIC %sh 
// MAGIC for b in bank.csv vehicles.csv characters.csv planets.csv species.csv; do
// MAGIC   curl -O "https://raw.githubusercontent.com/masfworld/datahack_docker/master/zeppelin/data/${b}"
// MAGIC done

// COMMAND ----------

dbutils.fs.mkdirs("/dataset")
Seq("bank.csv", "vehicles.csv", "characters.csv", "planets.csv", "species.csv").foreach( b =>
    dbutils.fs.cp(s"file:/databricks/driver/$b", s"dbfs:/dataset/$b")
)

// COMMAND ----------

dbutils.fs.ls("/dataset")

// COMMAND ----------

// MAGIC %md
// MAGIC # 1 - Window Functions
// MAGIC
// MAGIC Tip: Remember the spark reference --> https://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#window-functions

// COMMAND ----------

// MAGIC %md
// MAGIC ## 1.1 - Basic window example
// MAGIC

// COMMAND ----------

// Let's display our example dataset
dbutils.fs.head("/dataset/bank.csv", 512)

// COMMAND ----------

import org.apache.spark.sql.functions._

// Now we read it as a dataframe
val bankDF = spark.read
  .option("sep", ";")
  .option("inferSchema", "true")
  .option("header", "true")
  .csv("/dataset/bank.csv")

display(bankDF)

// COMMAND ----------

// We need to import the Window from expressions
import org.apache.spark.sql.expressions.Window

// We can define a static window with the partition/ordering and use it later
// You can define the Window object directly later if you want, no need to create a variable for it
val windowByJobSortedByAge = Window
  // Let's partition by Job, that means the window will apply to all rows where Job has the same value
  .partitionBy("job")
  // For each window partition, let's sort the rows based on balance descending and age ascending
  .orderBy(desc("balance"), asc("age"))

val bankOrderedDF = bankDF
  // Let's use the window function to create a new column!
  // This contains the row number of the partition, that is, for each "job", sorted by age
  .withColumn("balance_order_for_the_job", row_number().over(windowByJobSortedByAge))
  // Now we can keep, let's say, the top 2 balances for the job
  // Due to the window function, we'll pick the youngest age if there's a tie
  .filter(col("balance_order_for_the_job") <= 2)
  .select("job", "age", "balance")
  .orderBy("job", "age")

// Now we can display two entries for each job, for the top two balances, and the age
display(bankOrderedDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #1 - Balances by marital status
// MAGIC
// MAGIC Using Dataframe built from "bank.csv" file, obtain the top 3 of maximum balances for each marital status<br>
// MAGIC Show the result with marital, job, age and balance<br>
// MAGIC Define the window inline (No separate variable)

// COMMAND ----------

val topThreePerMaritalDF = bankDF
  .withColumn("top_three_marital", row_number().over(Window.partitionBy("marital").orderBy(desc("balance"))))
  .filter(col("top_three_marital") <= 3)
  .select("marital", "job", "age", "balance")

display(topThreePerMaritalDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #2 - Cost in credits
// MAGIC ---
// MAGIC Load file "vehicle.csv" in a DataFrame
// MAGIC

// COMMAND ----------

// Let's see what's this about
dbutils.fs.head("/dataset/vehicles.csv", 512)

// COMMAND ----------

// Load the CSV file as a DF (C'mon by now you're an expert on this already!)
val vehiclesDF = spark.read
  .option("inferSchema", "true")
  .option("header", "true")
  .csv("/dataset/vehicles.csv")

display(vehiclesDF)

// COMMAND ----------

// MAGIC %md
// MAGIC **TASK:** 
// MAGIC
// MAGIC Filter out the rows from the dataframe where cost in credits is "NA"
// MAGIC
// MAGIC For the rest of the rows, transform the dataframe to include two new columns:
// MAGIC - min_cost --> That's the minimum cost in credits *for the same vehicle class*
// MAGIC - cost_difference --> The difference between cost in credits and the min cost
// MAGIC
// MAGIC Also, display the table with the following column order:
// MAGIC - vehicle_class
// MAGIC - cost_in_credits
// MAGIC - min_cost
// MAGIC - cost_difference
// MAGIC - Every other column in the dataframe which has not been displayed already
// MAGIC
// MAGIC Sort the results by vehicle_class and cost_in_credits

// COMMAND ----------

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

val costOfVehiclesDF = vehiclesDF
  .filter($"cost_in_credits" =!= "NA") // Filtrar filas con "NA"
  .withColumn("cost_in_credits", col("cost_in_credits").cast(IntegerType)) // Convertir a IntegerType
  .withColumn("min_cost", min("cost_in_credits").over(Window.partitionBy("vehicle_class"))) // Definir la ventana inline
  .withColumn("cost_difference", col("cost_in_credits") - col("min_cost")) // Calcular la diferencia
  .select("vehicle_class","cost_in_credits","min_cost","cost_difference") // Reordenar las columnas
  .orderBy("vehicle_class", "cost_in_credits") // Ordenar el resultado

display(costOfVehiclesDF)

// COMMAND ----------

// MAGIC %md
// MAGIC # 2 - Joins

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #3 - Back to the galaxy
// MAGIC ---
// MAGIC * Create Dataframes from both "characters.csv" and "planets.csv"
// MAGIC * Get the gravity of the planet for each character
// MAGIC * Select only the name and planet (with its gravity) of the character
// MAGIC

// COMMAND ----------

// You know what to do
val charactersDF = spark.read
  .option("inferSchema", "true")
  .option("header", "true")
  .csv("/dataset/characters.csv")

display(charactersDF)

// COMMAND ----------

// What can I say...
val planetsDF = spark.read
  .option("inferSchema", "true")
  .option("sep", ";")
  .option("header", "true")
  .csv("/dataset/planets.csv")

display(planetsDF)

// COMMAND ----------

import org.apache.spark.sql.functions._

// Rename "name" column in planetsDF to avoid ambiguity
//val planetsRenamedDF = planetsDF.withColumnRenamed("name", "planet_name")

// Perform the join
val resultDF = charactersDF
  .join(planetsDF, charactersDF("homeworld") === planetsDF("name")) 
  .select(
    charactersDF("name").alias("character_name"), 
    planetsDF("name"), 
    planetsDF("gravity")) 

display(resultDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands on #4 - A tale of joins
// MAGIC ---
// MAGIC Review the execution plan for the previous exercise<br>
// MAGIC *Note: Just go to "DataFrame/SQL" tab and click on the latest query in the Spark UI*<br>
// MAGIC <br>
// MAGIC What type of join is being executed? Why?

// COMMAND ----------

// MAGIC %md
// MAGIC After reviewing the execution plan, execute the following instructions:

// COMMAND ----------

// Let's disable a pair of things...
// para optimizar ciertos joins 
// Disables Adaptive Query Execution (AQE), meaning Spark won't dynamically optimize join strategies at runtime.
// Disables broadcast joins, forcing Spark to use shuffle hash join or sort merge join, depending on the dataset size.
// This means the execution plan will likely show a shuffle or sort merge join instead of a broadcast join.
spark.conf.set("spark.sql.adaptive.enabled",false)
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", '0')

// COMMAND ----------

// MAGIC %md
// MAGIC **Execute again the *last cell of Hands on #3*** and review the execution plan. What is happening?
// MAGIC
// MAGIC Let's see in the following chapter!!

// COMMAND ----------

// MAGIC %md
// MAGIC # 3 - Adaptative Query Execution - AQE

// COMMAND ----------

// MAGIC %md
// MAGIC ## 3.1 - Avoiding excessive partitions
// MAGIC ---
// MAGIC One of the key features of AQE is coalescing the number of partitions based on the data.<br>
// MAGIC Spark will, by default, always create a *spark.sql.shuffle.partitions* number of partitions after shuffling<br>
// MAGIC AQE will though reduce that number of partitions in case they are excessive. Let's see!
// MAGIC

// COMMAND ----------

// Let's ensure AQE is disabled
spark.conf.set("spark.sql.adaptive.enabled", false)
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", false)

// COMMAND ----------

import spark.implicits._

// Let's just create some small DF
val sampleDF = spark.sparkContext.parallelize(Seq(
  ("James","Sales","NY",90000,34,10000),
  ("Michael","Sales","NY",86000,56,20000),
  ("Robert","Sales","CA",81000,30,23000),
  ("Maria","Finance","CA",90000,24,23000),
  ("Raman","Finance","CA",99000,40,24000),
  ("Scott","Finance","NY",83000,36,19000),
  ("Jen","Finance","NY",79000,53,15000),
  ("Jeff","Marketing","CA",80000,25,18000),
  ("Kumar","Marketing","NY",91000,50,21000)))
  .toDF("name","department","zip","max_salary","age","min_salary")

// GroupBy and count will shuffle the data and generate a spark.sql.shuffle.partitions number of partitions
val groupedDF = sampleDF.groupBy("department").count()
groupedDF.show(10, false)

// We can see here how many partitions this DF has (We have to access through the RDD underneath)
println(s"Num partitions: ${groupedDF.rdd.getNumPartitions}")

// COMMAND ----------

// Let's enable AQE again, and in particular the feature to coalesce partitions
spark.conf.set("spark.sql.adaptive.enabled",true)
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", true)

// If we repeat the same shuffling again...
val groupedDF = sampleDF.groupBy("department").count()
groupedDF.show(10, false)

// Look who got optimized!!
println(s"Num partitions: ${groupedDF.rdd.getNumPartitions}")
