// Databricks notebook source
// MAGIC %md
// MAGIC # Examen Spark
// MAGIC ## Samuel Arteaga López

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 1

// COMMAND ----------

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row

// Crear sesión de Spark 
val spark = SparkSession.builder.appName("Examen").getOrCreate()

// Definir el esquema de los datos
val vuelos = StructType(Array(
  StructField("id_vuelo", IntegerType),
  StructField("id_aerolinea", IntegerType),
  StructField("origen", StringType),
  StructField("destino", StringType),
  StructField("duracion", IntegerType),
  StructField("capacidad", IntegerType),
  StructField("fecha", StringType)
))

val aerolineas = StructType(Array(
  StructField("id_aerolinea", IntegerType),
  StructField("nombre_aerolinea", StringType),
  StructField("pais_origen", StringType)
))

val pasajeros = StructType(Array(
  StructField("id_vuelo", IntegerType),
  StructField("id_pasajero", IntegerType),
  StructField("nombre_pasajero", StringType),
  StructField("clase", StringType),
))

// COMMAND ----------

val datosVuelos = Seq(
  Row(1,101, "JFK", "LAX", 360, 180, "2024-03-01"),
  Row(2,102, "LAX", "ORD", 270, 200, "2024-03-02"),
  Row(3,101, "ORD", "MIA", 180, 150, "2024-03-03"),
  Row(4,103, "MIA", "ATL", 120, 220, "2024-03-04"),
  Row(5,104, "ATL", "DFW", 150, 160, "2024-03-05"),
  Row(6,105, "DFW", "SFO", 210, 190, "2024-03-06"),
  Row(7,102, "SFO", "SEA", 120, 170, "2024-03-07"),
  Row(8,101, "SEA", "JFK", 330, 175, "2024-03-08")
)

val datosAerolineas = Seq(
  Row(101, "American Airlines", "USA"),
  Row(102, "Delta Airlines", "USA"),
  Row(103, "United Airlines", "USA"),
  Row(104, "Lufthansa", "Germany"),
  Row(105, "Air France", "France")
)

val datosPasajeros = Seq(
  Row(1, 1001, "Carlos López", "Ecomómica"),
  Row(2, 1002, "Ana Martínez", "Business"),
  Row(3, 1003, "Luis Fernández", "Ecomómica"),
  Row(4, 1004, "Sofía Ramírez", "Primera Clase"),
  Row(5, 1005, "Miguel Torres", "Ecomómica"),
  Row(5, 1006, "Elena Ríos", "Business"),
  Row(5, 1007, "Pedro Suárez", "Ecomómica"),
  Row(5, 1008, "Lucía Gómez", "Ecomómica")
)

// Crear el DataFrame con el esquema
val dfVuelos = spark.createDataFrame(spark.sparkContext.parallelize(datosVuelos), vuelos)
val dfAerolineas = spark.createDataFrame(spark.sparkContext.parallelize(datosAerolineas), aerolineas)
val dfPasajeros = spark.createDataFrame(spark.sparkContext.parallelize(datosPasajeros), pasajeros)

// Mostrar los datos cargados
dfVuelos.show()
dfAerolineas.show()
dfPasajeros.show()

// COMMAND ----------

// MAGIC %md
// MAGIC Creamos las tablas para ser transformadas después a dataframe, seguidamente lo rellenamos con los datos proporcionados en el examen

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 2

// COMMAND ----------

val vuelosConAerolineas = dfVuelos
  .join(dfAerolineas, dfVuelos("id_aerolinea") === dfAerolineas("id_aerolinea")) 

display(vuelosConAerolineas)

// COMMAND ----------

//val vuelosYAerolineasConPasajeros = vuelosConAerolineas
  //.join(vuelosConAerolineas, vuelosConAerolineas("id_vuelo") === dfPasajeros("id_vuelo")) 

  val vuelosYAerolineasConPasajeros = vuelosConAerolineas
  .join(dfPasajeros, "id_vuelo")

display(vuelosYAerolineasConPasajeros)

// COMMAND ----------

// MAGIC %md
// MAGIC Creamos la unión de el dataframe de vuelos y aerolíneas para después por id_aerolinea para luego unirlo con el de pasajeros por id_vuelo

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 3

// COMMAND ----------

vuelosYAerolineasConPasajeros.createOrReplaceTempView("vuelosYAerolineasConPasajeros")

val primerApartadoScala = vuelosYAerolineasConPasajeros
  .groupBy($"nombre_aerolinea")
  .agg(sum($"id_pasajero").alias("num_pasajeros"))
  .select($"nombre_aerolinea", $"num_pasajeros")
  .orderBy(desc("num_pasajeros"))

primerApartadoScala.show()


/*val primerApartado = spark.sql(
  """
  SELECT nombre_aerolinea, SUM(id_pasajero) AS num_pasajeros
  FROM vuelosYAerolineasConPasajeros
  GROUP BY nombre_aerolinea
  ORDER BY num_pasajeros DESC
  """
)

primerApartado.show()
*/

val segundaApartadoScala = vuelosYAerolineasConPasajeros
  .groupBy($"nombre_aerolinea")
  .agg(avg($"duracion").alias("media_vuelo"))
  .select($"nombre_aerolinea", $"media_vuelo")
  .orderBy(desc("media_vuelo"))

/*val segundoApartado = spark.sql(
  """
  SELECT nombre_aerolinea, AVG(duracion) AS media_vuelo
  FROM vuelosYAerolineasConPasajeros
  GROUP BY nombre_aerolinea
  ORDER BY media_vuelo DESC
  """
)*/

//segundoApartado.show()
segundaApartadoScala.show()


val tercerApartadoScala = vuelosYAerolineasConPasajeros
  .groupBy($"nombre_aerolinea")
  .agg(count($"id_vuelo").alias("numero_vuelos"))
  .select($"nombre_aerolinea", $"numero_vuelos")
  .orderBy(desc("numero_vuelos"))

/*val tercerApartado = spark.sql(
  """
  SELECT nombre_aerolinea, COUNT(id_vuelo) AS numero_vuelos
  FROM vuelosYAerolineasConPasajeros
  GROUP BY nombre_aerolinea
  ORDER BY numero_vuelos DESC
  """
)*/

//tercerApartado.show()
tercerApartadoScala.show()


val cuartoApartadoScala = vuelosYAerolineasConPasajeros
  .groupBy($"nombre_aerolinea")
  .agg(count($"origen").alias("origen_mas_comun"), count($"destino").alias("destino_mas_comun"))
  .select($"nombre_aerolinea", $"origen_mas_comun", $"destino_mas_comun")
  .orderBy($"origen_mas_comun", $"destino_mas_comun")

/*val cuartoApartado = spark.sql(
  """
  SELECT nombre_aerolinea, COUNT(origen) AS origen_mas_comun , COUNT(destino) AS destino_mas_comun
  FROM vuelosYAerolineasConPasajeros
  GROUP BY nombre_aerolinea
  ORDER BY origen_mas_comun, destino_mas_comun DESC
  """
)*/
cuartoApartadoScala.show()
//cuartoApartado.show()

// COMMAND ----------

// MAGIC %md
// MAGIC 1. Determinamos cuántos pasajeros ha transportado cada aerolínea, siendo Lufthansa la que más ha transportado, 4026 
// MAGIC 2. Calculamos la duración promedio de los vuelos por aerolínea, donde la duración llega a los 270 minutos, siendo la más baja de 2 horas
// MAGIC 3. Mostramos la aerolínea con más vuelos operando, en este caso Lufthansa 
// MAGIC 4. Encontramos la ruta más frecuente, dado que no se repiten origenes ni destinos, es por ello que encontramos los resultados obtenidos 

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 4

// COMMAND ----------

val ejercicio4Scala = vuelosYAerolineasConPasajeros
  .groupBy($"nombre_aerolinea", $"id_pasajero", $"capacidad")
  .agg((count($"id_pasajero").alias("num_pasajeros")), round((($"num_pasajeros" / $"capacidad")*100),2).alias("porcentaje_de_ocupacion"))
  .orderBy(desc("porcentaje_de_ocupacion"))

  
/*val ejercicio4 = spark.sql(
  """
  SELECT nombre_aerolinea, (COUNT(id_pasajero)) AS num_pasajeros, ROUND(((num_pasajeros / capacidad) * 100),2) AS porcentaje_de_ocupacion
  FROM vuelosYAerolineasConPasajeros
  GROUP BY nombre_aerolinea, id_pasajero, capacidad
  ORDER BY porcentaje_de_ocupacion DESC
  """
)*/

//ejercicio4.show()
ejercicio4Scala.show()

// COMMAND ----------

// MAGIC %md
// MAGIC Obtenemos el porcentaje de cada vuelo teniendo en cuenta la cantidad de pasajeros vs la capacidad total donde el mayor porcentaje se encuentra en American Airlines

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 5

// COMMAND ----------

val ejercicio5scala = vuelosYAerolineasConPasajeros
  .select($"nombre_aerolinea", col("destino")).distinct()  
  
/*val ejercicio5 = spark.sql(
  """
  SELECT DISTINCT destino, nombre_aerolinea
  FROM vuelosYAerolineasConPasajeros
  """
)*/

//ejercicio5.show()
ejercicio5scala.show()

// COMMAND ----------

// MAGIC %md
// MAGIC Obtenemos las aerolineas con más destinos únicos, cada aerolínea tiene destinos únicos, por ello, el resultado obtenido

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 6

// COMMAND ----------

val ejercicio6scala = vuelosYAerolineasConPasajeros
  .groupBy($"nombre_aerolinea")
  .agg(max($"duracion").alias("mayor_duracion"))
  .select($"mayor_duracion", $"nombre_aerolinea")
  .limit(1)


/*val ejercicio6 = spark.sql(
  """
  SELECT MAX(duracion) as mayor_duracion, nombre_aerolinea
  FROM vuelosYAerolineasConPasajeros
  GROUP BY nombre_aerolinea
  LIMIT 1
  """
)*/

//ejercicio6.show()
ejercicio6scala.show()

// COMMAND ----------

// MAGIC %md
// MAGIC Obtenemos la ruta con mayor duración y su respectiva aerolinea encargada con 360 minutos de duración

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 7

// COMMAND ----------

val ejercicio7scala = vuelosYAerolineasConPasajeros
  .groupBy($"clase")
  .agg(count($"id_pasajero").alias("num_pasajeros"))
  .select($"clase", $"num_pasajeros")

/*val ejercicio7 = spark.sql(
  """
  SELECT COUNT(id_pasajero) as num_pasajeros, clase
  FROM vuelosYAerolineasConPasajeros
  GROUP BY clase
  """
)*/

//ejercicio7.show()
ejercicio7scala.show()

// COMMAND ----------

// MAGIC %md
// MAGIC Obtenemos por cada clase de vuelo el número de pasajeros en ellas siendo la económica la que más contiene

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 8

// COMMAND ----------

val ejercicio8scala = vuelosYAerolineasConPasajeros
  .withColumn("Mes", trim(substring_index(substring_index($"fecha", "-",2),"-",-1)))
  .groupBy($"Mes")
  .agg(count($"id_vuelo").alias("num_vuelos"))
  .select($"num_vuelos", $"Mes")
  

/*val ejercicio8 = spark.sql(
  """
  SELECT COUNT(id_vuelo) as num_vuelos, TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(fecha,'-',2),'-',-1)) AS Mes 
  FROM vuelosYAerolineasConPasajeros
  GROUP BY Mes
  """
)*/

// https://stackoverflow.com/questions/38241002/split-a-string-to-only-use-the-middle-part-in-sql
//ejercicio8.show()
ejercicio8scala.show()

// COMMAND ----------

// MAGIC %md
// MAGIC Obtenemos el número de vuelos que se realizaron en cada mes, en este caso todos los vuelos se realizaron en Marzo

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 9

// COMMAND ----------

dfVuelos.createOrReplaceTempView("vuelos")
dfAerolineas.createOrReplaceTempView("aerolineas")
dfPasajeros.createOrReplaceTempView("pasajeros")

// COMMAND ----------

// MAGIC %md
// MAGIC Creamos las vistas temporales de cada uno de los tres dataframes con el método `createOrReplaceTempView`

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 10

// COMMAND ----------

// MAGIC %md
// MAGIC #### Informes detallados después de cada apartado