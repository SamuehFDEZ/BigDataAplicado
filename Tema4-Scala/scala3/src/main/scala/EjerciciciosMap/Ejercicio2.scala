package esp.scala3.app
package EjerciciciosMap

object Ejercicio2 {
    val frutas = List("Manzana", "Pera", "Naranja", "Uva")
    val precios = List(1, 2, 5, 7)
    val cantidadFruta = List(2, 4, 10, 20)

    def main(args: Array[String]): Unit = {
        val mensajes = frutas.indices
          .filter(fruta => precios(fruta) > 3)
          .map(fruta => s"Fruta: ${frutas(fruta)}, Precio total: ${precios(fruta) * cantidadFruta(fruta)}")

        mensajes.foreach(println)
    }
}