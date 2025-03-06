// Databricks notebook source
// MAGIC %md
// MAGIC # Analizar los salarios de sus empleados en diferentes departamentso para obtener información clave sobre la distribución de los sueldos.

// COMMAND ----------

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row

// Crear sesión de Spark 
val spark = SparkSession.builder.appName("AnalisisSalarios").getOrCreate()

// Definir el esquema de los datos
val schema = StructType(Array(
  StructField("id", IntegerType),
  StructField("Nombre del empleado", StringType),
  StructField("Departamento", StringType),
  StructField("Salario", DoubleType),
))


// COMMAND ----------

// Datos de empleados
val datos = Seq(
  Row(1,"Ana Soria Serrano", "Ventas", 3000.0),
  Row(2,"Luis García Pérez", "Administración", 2500.0),
  Row(3,"Roberto Sánchez Sánchez", "Ventas", 3500.0),
  Row(4,"Pedro Serrano Martínez", "RRHH", 3200.0),
  Row(5,"Alejandro Giménez Sánchez", "Administración", 2700.0)
)


// COMMAND ----------

// Crear el DataFrame con el esquema
val df = spark.createDataFrame(spark.sparkContext.parallelize(datos), schema)
// Mostrar los datos cargados
df.show()

// COMMAND ----------

// Agrupar por departamento y calcular salario promedio
val dfAgrupado = df
  .groupBy("Departamento")
  .agg(avg("Salario").as("Media_Salario"))
  
// Mostrar los resultados
dfAgrupado.show()

// COMMAND ----------

// Ordenar de mayor a menor salario promedio
val dfOrdenado = dfAgrupado.orderBy(desc("Media_Salario"))

// Mostrar resultados ordenados
dfOrdenado.show()

// COMMAND ----------

// Crear vista temporal para consultas SQL 
// Antes estaba como dfAgrupado esto es un problema porque Media_Salario ya está aplicado
df.createOrReplaceTempView("AnalisisSalarios")

// COMMAND ----------

// Consulta SQL en Spark
// Ejecutar una consulta SQL que devuelva el salario promedio por departamento, ordenado de mayor a menor
val dfSQL = spark.sql("SELECT Departamento, avg(Salario) AS Media_Salarios FROM AnalisisSalarios GROUP BY Departamento ORDER BY Media_Salarios DESC")
// Mostrar resultados de SQL
dfSQL.show()
// SELECT Departamento, avg('Salario') as Media_Salarios FROM Salarios GROUP BY Departamento ORDER BY Media_Salarios DESC
