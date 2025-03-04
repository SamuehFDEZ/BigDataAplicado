// Databricks notebook source
//Analizar los salarios de sus empleados en diferentes departamentso para obtener información clave sobre la distribución de los sueldos.


// COMMAND ----------
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row

// Crear sesión de Spark 
val spark = SparkSession.builder.appName("AnalisisSalarios").getOrCreate()

// Definir el esquema de los datos
val schema =


// COMMAND ----------
// Datos de empleados
val datos =


// COMMAND ----------

// Crear el DataFrame con el esquema
val df = spark.createDataFrame(spark.sparkContext.parallelize(datos), schema)
// Mostrar los datos cargados
df.show()

// COMMAND ----------
// Agrupar por departamento y calcular salario promedio
val dfAgrupado = 

// Mostrar los resultados
dfAgrupado.show()

// COMMAND ----------
// Ordenar de mayor a menor salario promedio
val dfOrdenado =

// Mostrar resultados ordenados
dfOrdenado.show()

// COMMAND ----------
// Crear vista temporal para consultas SQL

// COMMAND ----------
// Consulta SQL en Spark
val dfSQL = spark.sql(...)
// Mostrar resultados de SQL
dfSQL.show()


