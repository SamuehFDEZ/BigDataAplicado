// Databricks notebook source
// MAGIC %md
// MAGIC # 1 - Intro & ACID

// COMMAND ----------

// MAGIC %md
// MAGIC ## 1.1 - ACID Atomicity
// MAGIC https://docs.databricks.com/en/lakehouse/acid.html#atomicity

// COMMAND ----------

//We will be storing data into the same path, let's make it clean
val filePath = "/mnt/delta/atomicity"
dbutils.fs.rm(filePath, true)

// Now we create a range between 100 and 200 and we save it as delta format
val data = spark.range(100, 200)
data.write.format("delta").mode("overwrite").save(filePath)

// COMMAND ----------

// We successfully saved all 100 rows
spark.read.format("delta").load(filePath).count()

// Let's display some
display(spark.read.format("delta").load(filePath))

// COMMAND ----------

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

// Let's create a UDF that changes the type of an input from long to int, but sadly it fails to do that for the number 60 in particular
// https://imgur.com/gallery/vdLE8dJ
val intToLongFailingIn60 = udf[Int, Long]((x: Long) => {
  if (x == 60) throw new Exception("Error in: " + x)
  x.toInt
})

// Now let's try to save values from 50 to 200 into the table...
spark.range(50,200)
.withColumn("id", intToLongFailingIn60(col("id")))
.write.format("delta")
.mode("overwrite")
// We can even overwrite the schema atomically. Try commenting out this line. The error is different this time!
.option("overwriteSchema", "true")
.save(filePath)

// COMMAND ----------

// No more data has been saved in the table. Not even those rows from 50 to 59
spark.read.format("delta").load(filePath).count()

// COMMAND ----------

// MAGIC %md
// MAGIC ## 1.2 - Converting to Delta

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Let's drop this table if it exists (So that we can replay these cells)
// MAGIC DROP TABLE IF EXISTS events

// COMMAND ----------

// Also we define a delta path for that events table and we ensure it's empty
val eventsDeltaPath = "/mnt/delta/events"
dbutils.fs.rm(eventsDeltaPath, true)

// COMMAND ----------

// Let's read from a json path predefined by databricks into our selected delta path for the events
spark
  .read.json("/databricks-datasets/structured-streaming/events/")
  .write.format("delta").save(eventsDeltaPath)

// And now we can define a table in the metadata for that delta location
spark.sql(s"CREATE TABLE events USING DELTA LOCATION '$eventsDeltaPath'")

// COMMAND ----------

// MAGIC %sql
// MAGIC -- We can see the action table contains 50K Open events and 50K closed events
// MAGIC SELECT action, count(*)
// MAGIC FROM events
// MAGIC GROUP BY action

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Let's peek into the table. It contains two columns: Action (Open/Close) and time (Unix time)
// MAGIC SELECT * FROM events

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Let's update (!) the table to set all events to Open
// MAGIC UPDATE events SET action = 'Open' WHERE action = 'Close'

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands-on #1 - Updating a table with the API
// MAGIC ---
// MAGIC
// MAGIC Using Delta Table Scala API (No SQL!):
// MAGIC 1. Load deltatable events in a Delta Table variable
// MAGIC 2. Update table time = 0 where Action = "Open"
// MAGIC
// MAGIC *Hint: Pretty much this --> https://docs.databricks.com/en/delta/tutorial.html#update-a-table*

// COMMAND ----------

import io.delta.tables._

// Load deltatable events in a Delta Table variable
val eventsDT = DeltaTable.forPath(spark, eventsDeltaPath)

// Let's see what we've got
println("#### BEFORE ####")
eventsDT.toDF.show(10, false)

// Update table time = 0 where Action = "Open"
eventsDT.update(                
  col("Action") === "Open",
  Map("time" -> lit(0))
)

// Let's see what we've got now
println("#### AFTER ####")
eventsDT.toDF.show(10, false)



// COMMAND ----------

// MAGIC %md
// MAGIC # 2 - Time Travel

// COMMAND ----------

// MAGIC %md
// MAGIC ## 2.1 - Time travel in events table
// MAGIC ---
// MAGIC https://delta.io/blog/2023-02-01-delta-lake-time-travel/

// COMMAND ----------

// MAGIC %sql
// MAGIC -- The describe history command allows you to... drum roll... describe history!
// MAGIC DESCRIBE HISTORY events

// COMMAND ----------

// We can query the data as of any particular point in time by specifiying a "timestampAsOf" option
val df = spark.read
  .format("delta")
  // Hint: Add the correct timestamp here you want to travel to after you get an error. You'll see there if there's any timezone considerations
  .option("timestampAsOf", "2023-10-01T00:00:00.000+0000")
  .load(eventsDeltaPath)

val groupedDF = df.groupBy("action").count()

display(groupedDF)

// COMMAND ----------

// MAGIC %sql
// MAGIC -- We can also time travel to any particular version, and also we can time travel in SQL
// MAGIC SELECT action, count(*)
// MAGIC FROM events VERSION AS OF 1
// MAGIC GROUP BY action

// COMMAND ----------

// MAGIC %md
// MAGIC ## 2.2 - Flights

// COMMAND ----------

// Here we have the departure delays dataset
val tripdelaysFilePath = "/databricks-datasets/flights/departuredelays.csv"
dbutils.fs.ls("/databricks-datasets/flights")

// COMMAND ----------

import org.apache.spark.sql.types._

val schema = StructType(Seq( 
  StructField("date", LongType, true), 
  StructField("delay", IntegerType, true), 
  StructField("distance", IntegerType, true), 
  StructField("origin", StringType, true), 
  StructField("destination", StringType, true) 
  ))

// Let's load it into a DataFrame
val departureDelays = spark.read
.option("header", "true")
.schema(schema)
.csv(tripdelaysFilePath)

  
display(departureDelays)

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Dropping the table
// MAGIC DROP TABLE IF EXISTS flights

// COMMAND ----------

// Save it as a managed delta table
departureDelays
  .write 
  .mode("overwrite") 
  .format("delta") 
  .saveAsTable("flights")

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Let's display what's in there
// MAGIC Select * 
// MAGIC from flights
// MAGIC limit 10

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Also let's describe the history
// MAGIC DESCRIBE HISTORY flights

// COMMAND ----------

// MAGIC %sql
// MAGIC -- We can update the delays (We can dream, uh?)
// MAGIC UPDATE flights SET delay = 0

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Now we can see the update took place
// MAGIC Select * 
// MAGIC from flights
// MAGIC limit 10

// COMMAND ----------

// MAGIC %sql
// MAGIC -- The describe extended command shows the table metadata
// MAGIC DESCRIBE EXTENDED flights

// COMMAND ----------

// MAGIC %sql
// MAGIC -- We can describe the updated history too
// MAGIC DESCRIBE HISTORY flights

// COMMAND ----------


// Because it is a managed table, we don't know where the data was saved to
// That doesn't mean we can't know!
val tablePath = spark.sql("describe extended flights").where(col("col_name") === "Location").select("data_type").as[String].collect.head

// We can load the first version of the dataset
val flightsDF = spark.read
  .format("delta")
  .option("versionAsOf", "0")
  .load(tablePath)

// COMMAND ----------

// And we can even restore it!
flightsDF
  .write
  .format("delta")
  .mode("overwrite")
  .save("/user/hive/warehouse/flights")

// COMMAND ----------

// MAGIC %sql
// MAGIC -- There, restored!
// MAGIC Select * 
// MAGIC from flights
// MAGIC limit 10

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Note: We're not losing history. WRITE is just another operation added to the history.
// MAGIC DESCRIBE HISTORY flights

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Restoring initial state from SQL too
// MAGIC INSERT OVERWRITE flights
// MAGIC SELECT *
// MAGIC FROM flights
// MAGIC VERSION AS OF 0

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Again, just another operation, identical to the previous one, but from SQL. No history lost.
// MAGIC DESCRIBE HISTORY flights

// COMMAND ----------

// MAGIC %md
// MAGIC # 3 - Schema Evolution

// COMMAND ----------

// MAGIC %md
// MAGIC ## 3.1 - Lending club
// MAGIC
// MAGIC

// COMMAND ----------

// Let's define our next dataset
val loanStatsPath = "/databricks-datasets/samples/lending_club/parquet/"

// Notice the part files
dbutils.fs.ls(loanStatsPath).foreach(println)

// COMMAND ----------


// Let's open this parquet file
val rawLoanStatsDF = spark.read.parquet(loanStatsPath)
display(rawLoanStatsDF)

// COMMAND ----------

rawLoanStatsDF.count()

// COMMAND ----------

// Reduce the amount of data
val split = rawLoanStatsDF.randomSplit(Array(0.01, 0.99), 123)
var loanStatsDF = split(0)
val loanStatsRestDF = split(1)

// Select only the columns needed
val loanStatsReducedDF = loanStatsDF.select("addr_state", "loan_status")

// Create loan by state
val loanByStateDF = loanStatsReducedDF.groupBy("addr_state").count()

// Create table
loanByStateDF.createOrReplaceTempView("loan_by_state")


// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT *
// MAGIC FROM loan_by_state
// MAGIC LIMIT 10

// COMMAND ----------

// Configure Delta Lake Silver Path
val deltaLakeSilverPath = "/ml/loan_by_state_delta"

// Remove folder if it exists
dbutils.fs.rm(deltaLakeSilverPath, true)

// COMMAND ----------

// MAGIC %sql 
// MAGIC -- Current example is creating a new table instead of in-place import so will need to change this code
// MAGIC DROP TABLE IF EXISTS loan_by_state_delta;
// MAGIC
// MAGIC CREATE TABLE loan_by_state_delta
// MAGIC USING delta
// MAGIC LOCATION '/ml/loan_by_state_delta'
// MAGIC AS SELECT * FROM loan_by_state;
// MAGIC
// MAGIC -- View Delta Lake table
// MAGIC SELECT * FROM loan_by_state_delta

// COMMAND ----------

val loansDF = sql("""
            SELECT addr_state, CAST(rand(10)*count as bigint) AS count,
            CAST(rand(10) * 10000 * count AS double) AS amount
            FROM loan_by_state_delta
            """)
display(loansDF)

// COMMAND ----------

// Show original DataFrame's schema
loanByStateDF.printSchema()

// COMMAND ----------

// Show new DataFrame's schema
loansDF.printSchema()

// COMMAND ----------

// Merging schemas needs to be explicitly allowed, otherwise...
loans.write.format("delta")
           .mode("append")
           .save("/ml/loan_by_state_delta")

// COMMAND ----------

// Adding the mergeSchema option everything works
loans.write.format("delta")
           .option("mergeSchema", "true")
           .mode("append")
           .save("/ml/loan_by_state_delta")

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT *
// MAGIC from loan_by_state_delta
// MAGIC limit 10

// COMMAND ----------

import org.apache.spark.sql.functions._

// Create loan by state
val loanByStateAmountDF = rawLoanStatsDF.groupBy("addr_state").agg(avg("loan_amnt").alias("avg_amnt"))
display(loanByStateAmountDF)

// COMMAND ----------

// OverwriteSchema doesn't work with append by the way
// Note: https://medium.com/@amany.m.abdelhalim/appending-overwriting-with-different-schema-to-delta-lake-vs-parquet-6b39c4a5d5dc
loanByStateAmountDF.write.format("delta")
           .option("overwriteSchema", "true")
           .mode("append")
           .save("/ml/loan_by_state_delta")

// COMMAND ----------

// It does work with overwrite though
loanByStateAmountDF.write.format("delta")
           .option("overwriteSchema", "true")
           .mode("overwrite")
           .save("/ml/loan_by_state_delta")

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT *
// MAGIC FROM loan_by_state_delta
// MAGIC LIMIT 10

// COMMAND ----------

// MAGIC %sql
// MAGIC DESCRIBE HISTORY loan_by_state_delta

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT *
// MAGIC FROM loan_by_state_delta VERSION AS OF 1

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands-on #2 - Grouping flights
// MAGIC ---
// MAGIC Do the following tasks over flight table built in Time Travel section:
// MAGIC 1. Get the average of distances for each origin airport. Store the result in a new delta table called "flight_grouped"
// MAGIC 2. Create new dataframe grouping flights data by origin with only the average of the delay
// MAGIC 3. Merge the information generated in point 2 over "flight_grouped" table
// MAGIC 4. Restore "flight_grouped" table to the previous version

// COMMAND ----------

// MAGIC %sql
// MAGIC Select origin, avg(distance)
// MAGIC from flights
// MAGIC group by origin

// COMMAND ----------

// MAGIC %sql
// MAGIC -- Current example is creating a new table instead of in-place import so will need to change this code
// MAGIC DROP TABLE IF EXISTS flights_in_groups;
// MAGIC
// MAGIC CREATE TABLE flights_in_groups
// MAGIC USING delta
// MAGIC AS Select origin, avg(distance) as avg_distance
// MAGIC from flights
// MAGIC group by origin;
// MAGIC
// MAGIC -- View Delta Lake table
// MAGIC SELECT * FROM flights_in_groups

// COMMAND ----------

// MAGIC %sql
// MAGIC DESCRIBE extended flights_in_groups

// COMMAND ----------

val flightInGroupsDelayDF = sql("""
            SELECT origin, avg(delay) as avg_delay            
            FROM flights
            GROUP BY origin
            """)
display(flightInGroupsDelayDF)

// COMMAND ----------

flightInGroupsDelayDF.write.format("delta")
           .option("mergeSchema", "true")
           .mode("append")
           .save("/user/hive/warehouse/flights_in_groups")

// COMMAND ----------

// MAGIC %sql
// MAGIC
// MAGIC SELECT *
// MAGIC FROM flights_in_groups

// COMMAND ----------

// MAGIC %sql 
// MAGIC DESCRIBE HISTORY flights_in_groups

// COMMAND ----------

// We can restore the initial version overwriting the schema again
spark
  .read
  .format("delta")
  .option("versionAsOf", 0)
  .load("/user/hive/warehouse/flights_in_groups")
  .write
  .format("delta")
  .option("overwriteSchema", "true")
  .mode("overwrite")
  .save("/user/hive/warehouse/flights_in_groups")

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT *
// MAGIC FROM flights_in_groups

// COMMAND ----------

// MAGIC %md
// MAGIC # 4 - Tables

// COMMAND ----------

// MAGIC %md
// MAGIC ## 4.1 Partitioning & table ops
// MAGIC ---
// MAGIC *https://docs.delta.io/latest/delta-batch.html#partition-data*

// COMMAND ----------

// Let's display our departureDelays
display(departureDelays)

// COMMAND ----------

// MAGIC %sql
// MAGIC DROP TABLE IF EXISTS flights_partitioned

// COMMAND ----------

// Let's create a managed delta table with the flights partitioned by origin
departureDelays
  .write
  .mode("overwrite")
  .format("delta")
  .partitionBy("origin")
  .saveAsTable("flights_partitioned") // Managed table

// COMMAND ----------

// MAGIC %sql
// MAGIC SHOW TABLES

// COMMAND ----------

// MAGIC %sql
// MAGIC DESCRIBE DETAIL flights_partitioned

// COMMAND ----------

// MAGIC %sql
// MAGIC ALTER TABLE flights_partitioned ADD CONSTRAINT delayminimum CHECK (delay > -10000)

// COMMAND ----------

// MAGIC %sql
// MAGIC DESCRIBE flights_partitioned

// COMMAND ----------

// MAGIC %sql
// MAGIC INSERT INTO flights_partitioned VALUES (10112456, -20000, 1, "JFK", "JFK")

// COMMAND ----------

// MAGIC %md
// MAGIC # 5 - Upsert with Merge

// COMMAND ----------

// MAGIC %md
// MAGIC ## 5.1 - Online retail data

// COMMAND ----------

dbutils.fs.ls("/databricks-datasets/online_retail/data-001/data.csv")

// COMMAND ----------

val miniDataInputPath = "/databricks-datasets/online_retail/data-001/data.csv"
val basePath            = "/mnt/delta/python"
val deltaMiniDataPath   = basePath + "/customer-data-mini/"

val inputSchema = "InvoiceNo INT, StockCode STRING, Description STRING, Quantity INT, InvoiceDate STRING, UnitPrice DOUBLE, CustomerID INT, Country STRING"

// COMMAND ----------

val miniDataDF = spark.read
  .option("header", "true")
  .schema(inputSchema)
  .csv(miniDataInputPath)

miniDataDF
  .write
  .mode("overwrite")
  .format("delta")
  .save(deltaMiniDataPath)

// COMMAND ----------

// MAGIC %sql
// MAGIC DROP TABLE IF EXISTS customer_data_delta_mini

// COMMAND ----------

spark.sql(s"""
    CREATE TABLE customer_data_delta_mini
    USING DELTA
    LOCATION '${deltaMiniDataPath}'
  """)

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM customer_data_delta_mini LIMIT 10

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM customer_data_delta_mini WHERE CustomerID=17925

// COMMAND ----------

import org.apache.spark.sql.functions._


val customerSpecificDF = (miniDataDF
  .filter("CustomerID=17925")
  .withColumn("StockCode", lit(99999))
  .withColumn("InvoiceNo", col("InvoiceNo").cast("String"))
  )

spark.sql("DROP TABLE IF EXISTS customer_data_delta_to_upsert")
customerSpecificDF.write.saveAsTable("customer_data_delta_to_upsert")

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM customer_data_delta_to_upsert WHERE CustomerID=17925

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM customer_data_delta_mini WHERE CustomerID=17925

// COMMAND ----------

// MAGIC %sql
// MAGIC MERGE INTO customer_data_delta_mini
// MAGIC USING customer_data_delta_to_upsert
// MAGIC ON customer_data_delta_mini.CustomerID = customer_data_delta_to_upsert.CustomerID
// MAGIC WHEN MATCHED THEN
// MAGIC   UPDATE SET
// MAGIC     customer_data_delta_mini.StockCode = customer_data_delta_to_upsert.StockCode
// MAGIC WHEN NOT MATCHED
// MAGIC   THEN INSERT (InvoiceNo, StockCode, Description, Quantity, InvoiceDate, UnitPrice, CustomerID, Country)
// MAGIC   VALUES (
// MAGIC     customer_data_delta_to_upsert.InvoiceNo,
// MAGIC     customer_data_delta_to_upsert.StockCode,
// MAGIC     customer_data_delta_to_upsert.Description,
// MAGIC     customer_data_delta_to_upsert.Quantity,
// MAGIC     customer_data_delta_to_upsert.InvoiceDate,
// MAGIC     customer_data_delta_to_upsert.UnitPrice,
// MAGIC     customer_data_delta_to_upsert.CustomerID,
// MAGIC     customer_data_delta_to_upsert.Country)

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM customer_data_delta_mini WHERE CustomerID=17925

// COMMAND ----------

// MAGIC %md
// MAGIC ## Hands-on #3
// MAGIC ---
// MAGIC
// MAGIC Do the following tasks:
// MAGIC 1. Generate a delta table called "flights_grouped_by_delay" from DataFrame with the average of delay grouped by origin, built in a previous exercise in this notebook
// MAGIC 2. Generate a delta table called "flights_grouped" calculating the maximum delay for each origin airport
// MAGIC 2. Merge (using SQL) the table built in the point 1 and 2
// MAGIC
// MAGIC *Hints: <br>
// MAGIC https://docs.delta.io/latest/delta-update.html#language-scala* </br>
// MAGIC *https://docs.microsoft.com/en-us/azure/databricks/spark/latest/spark-sql/language-manual/delta-merge-into*
// MAGIC

// COMMAND ----------

val flightsGroupedDelay = sql("""
            SELECT origin, avg(delay) as avg_delay
            FROM flights
            GROUP BY origin
            """)
display(flightsGroupedDelay)

// COMMAND ----------

// MAGIC %sql
// MAGIC DROP TABLE IF EXISTS flights_grouped_by_delay

// COMMAND ----------

flightsGroupedDelay
  .write
  .format("delta")
  .saveAsTable("flights_grouped_by_delay")

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM flights_grouped_by_delay

// COMMAND ----------

// MAGIC %sql
// MAGIC DROP TABLE IF EXISTS flights_grouped

// COMMAND ----------

// MAGIC %sql
// MAGIC CREATE TABLE flights_grouped
// MAGIC USING delta
// MAGIC AS SELECT origin, max(delay) as max_delay
// MAGIC FROM flights
// MAGIC GROUP BY origin

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM flights_grouped

// COMMAND ----------

spark.sql("SET spark.databricks.delta.schema.autoMerge.enabled = true")

// COMMAND ----------

// MAGIC %sql
// MAGIC MERGE INTO flights_grouped
// MAGIC USING flights_grouped_by_delay
// MAGIC ON flights_grouped.origin = flights_grouped_by_delay.origin
// MAGIC WHEN MATCHED
// MAGIC   THEN UPDATE SET *

// COMMAND ----------

spark.sql("SET spark.databricks.delta.schema.autoMerge.enabled = false")

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT *
// MAGIC FROM flights_grouped

// COMMAND ----------

// MAGIC %md
// MAGIC # 6 - Optimization

// COMMAND ----------

// MAGIC %md
// MAGIC ## 6.1 - Optimization example

// COMMAND ----------

// MAGIC %fs rm -r /tmp/flights_parquet

// COMMAND ----------

// MAGIC %fs rm -r /tmp/flights_delta

// COMMAND ----------

val flights = spark.read.format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load("/databricks-datasets/asa/airlines/2008.csv")

// COMMAND ----------

display(flights)

// COMMAND ----------

flights.write.format("parquet").mode("overwrite").partitionBy("Origin").save("/tmp/flights_parquet")

// COMMAND ----------

import org.apache.spark.sql.functions._

val flights_parquet = spark.read.format("parquet").load("/tmp/flights_parquet")

display(flights_parquet.filter("DayOfWeek = 1").groupBy("Month","Origin").agg(count("*").alias("TotalFlights")).orderBy(col("TotalFlights").asc).limit(20))

// COMMAND ----------

// MAGIC %sql
// MAGIC SHOW TABLES

// COMMAND ----------

flights.write.format("delta").mode("overwrite").partitionBy("Origin").save("/tmp/flights_delta")

// COMMAND ----------

display(spark.sql("DROP TABLE  IF EXISTS flights"))

display(spark.sql("CREATE TABLE flights USING DELTA LOCATION '/tmp/flights_delta'"))

display(spark.sql("OPTIMIZE flights ZORDER BY (DayofWeek)"))

// COMMAND ----------

val flights_delta = spark.read.format("delta").load("/tmp/flights_delta")

display(flights_delta.filter("DayOfWeek = 1").groupBy("Month","Origin").agg(count("*").alias("TotalFlights")).orderBy(col("TotalFlights").asc).limit(20))

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT count(*) from flights
