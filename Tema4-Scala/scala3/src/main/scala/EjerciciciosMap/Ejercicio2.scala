package EjerciciciosMap

object Ejercicio2 {
    val frutas = List("Manzana", "Pera", "Naranja")
    val precios = List(1, 2, 5)
    val cantidadCompra = List(2, 4, 10)

    def main(args: Array[String]): Unit = {

        // 1. Filtrar los índices de las frutas cuyo precio por unidad sea mayor a 3
        val indicesFiltrados = precios.indices.filter(i => precios(i) > 3)

        // 2. Calcular el precio total para cada índice filtrado
        val preciosTotales = indicesFiltrados.map(i =>
            (frutas(i), precios(i) * cantidadCompra(i))
        )

        // 3. Generar la lista de mensajes en el formato requerido
        val mensajes = preciosTotales.map(par =>
            "Fruta: " + par._1 + ", Total: " + par._2
        )

        // Imprimir los resultados
        mensajes.foreach(println)
    }
}