// Databricks notebook source
// MAGIC %md
// MAGIC Una tienda desea analizar las ventas realizadas y su relación con los productos disponibles en el
// MAGIC  inventario. Se necesita calcular el total de ingresos por categoría de producto y ordenar los
// MAGIC  resultados de mayor a menor. Además, se debe habilitar la posibilidad de realizar consultas SQL
// MAGIC  sobre los datos almacenados.
// MAGIC  Se cuenta con dos conjuntos de datos de Ventas y productos siendo los siguientes:

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 1
// MAGIC  Cargar los datos de ventas y productos en DataFrames separados con la siguiente estructura:
// MAGIC  Ventas:<br><br> 
// MAGIC  id_venta (Int) → Iden ficador único de la venta.<br> 
// MAGIC  id_producto (Int) → Identificador del producto vendido.<br><br> 
// MAGIC  cantidad (Int) → Cantidad de productos vendidos.<br> <br> 
// MAGIC  precio_unitario (Double) → Precio unitario del producto.<br> <br> 
// MAGIC  Productos:<br> <br> 
// MAGIC  id_producto (Int) → Identificador único del producto.<br> <br> 
// MAGIC  nombre_producto (String) → Nombre del producto.<br> <br> 
// MAGIC  categoria (String) → Categoría del producto.<br> <br> 

// COMMAND ----------

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row

// Crear sesión de Spark 
val spark = SparkSession.builder.appName("VentasYProductos").getOrCreate()

// Definir el esquema de los datos
val ventas = StructType(Array(
  StructField("id_venta", IntegerType),
  StructField("id_producto", IntegerType),
  StructField("cantidad", IntegerType),
  StructField("precio_unitario", DoubleType),
))

val productos = StructType(Array(
  StructField("id_producto", IntegerType),
  StructField("nombre_producto", StringType),
  StructField("categoria", StringType),
))

val datosVentas = Seq(
  Row(1,101, 2, 15.0),
  Row(2,102, 1, 25.0),
  Row(3,101, 3, 15.0),
  Row(4,103, 5, 10.0),
  Row(5,104, 2, 30.0)
)

val datosProductos = Seq(
  Row(101, "Camiseta", "Ropa"),
  Row(102, "Zapatos", "Calzado"),
  Row(103, "Gorra", "Accesorios"),
  Row(104, "Chaqueta", "Ropa"),
)

// Crear el DataFrame con el esquema
val dfVentas = spark.createDataFrame(spark.sparkContext.parallelize(datosVentas), ventas)
val dfProductos = spark.createDataFrame(spark.sparkContext.parallelize(datosProductos), productos)

// Mostrar los datos cargados
dfVentas.show()
dfProductos.show()

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 2 
// MAGIC Realizar un Join entre las tablas Ventas y Productos usando id_producto, de modo que 
// MAGIC se pueda relacionar cada venta con su categoría.
// MAGIC

// COMMAND ----------

val resultDF = dfVentas
  .join(dfProductos, dfVentas("id_producto") === dfProductos("id_producto")) 

display(resultDF)

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 3 
// MAGIC Calcular los ingresos totales por categoría <br><br>
// MAGIC  • Multiplicar cantidad * precio_unitario para obtener el ingreso por cada venta.<br>
// MAGIC  • Agrupar por categoria y calcular la suma total de ingresos por cada categoría.<br>
// MAGIC  • Ordenar los resultados de mayor a menor ingreso total<br>

// COMMAND ----------

val ingresoPorVenta = resultDF
  .groupBy(col("categoria"))
  .agg(sum((col("cantidad") * col("precio_unitario"))).as("IngresoPorVenta"))
  .select("categoria")
  .orderBy("IngresoPorVenta")

ingresoPorVenta.show()

// COMMAND ----------

// MAGIC %md
// MAGIC ### Ejercicio 4: 
// MAGIC Habilitar consultas SQL sobre los datos:<br><br>
// MAGIC  • Registrar los DataFrames como vistas temporales.<br>
// MAGIC  • Ejecutar una consulta SQL que devuelva los ingresos totales por categoría, ordenados de 
// MAGIC mayor a menor.

// COMMAND ----------

resultDF.createOrReplaceTempView("resultDF")

val ingresoPorCategoria = spark.sql("""
  SELECT categoria, 
         SUM(cantidad * precio_unitario) AS IngresoPorVenta
  FROM resultDF
  GROUP BY categoria
  ORDER BY IngresoPorVenta DESC
""")
ingresoPorCategoria.show()


// COMMAND ----------

// MAGIC %md
// MAGIC  Según los resultados obtenidos responde a las siguientes preguntas:<br>
// MAGIC  • ¿Cuál es la categoría de productos con mayores ingresos?<br>
// MAGIC  Ropa<br>
// MAGIC  • ¿Cuánto ha generado cada categoría en total?<br>
// MAGIC  205<br>
// MAGIC  • ¿En qué categoría se realizan más ventas en términos de cantidad de productos vendidos<br>
// MAGIC  Ropa
