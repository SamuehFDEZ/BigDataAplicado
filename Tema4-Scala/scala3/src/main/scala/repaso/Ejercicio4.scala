package esp.scala3.app
package repaso

object Ejercicio4 extends App {
  case class Productos(nombre: String, precio: Double, categoria: String)

  val listaProductoss = List(
    Productos("Microondas", 80, "electrodomestico"),
    Productos("Libro", 60, "material"),
    Productos("Boli", 30, "material"),
    Productos("Lapiz", 120, "material"),
    Productos("Estuche", 51, "")
  )

  val listaMayoresDeEdad = listaProductoss
    .filter(_.precio >= 50)
    .map(p => "Nombre: " + p.nombre.toUpperCase()
      + ", Categoria: " + p.categoria)

  listaMayoresDeEdad.foreach(println)
}