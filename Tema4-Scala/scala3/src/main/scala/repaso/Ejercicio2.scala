package esp.scala3.app
package repaso

/* Ejercicio 2
   Imagina que estás desarrollando un sistema para una librería en línea donde cada libro tiene un título, un precio y un género
   (por ejemplo, "Ficción", "No Ficción", "Ciencia", etc.).
   El sistema debe permitir realizar varias operaciones sobre una lista de libros, tales como:
       filtrar los libros cuyo precio sea superior a 20, obtener los títulos de los libros de un género específico,
       expandir la lista de libros para incluir tanto el título como el género,
       aplicar un descuento del 10% a los libros cuyo precio sea superior a 50,
       y finalmente clasificar los libros según su género utilizando match.
   El objetivo es utilizar las funciones map, flatMap, filter y match para resolver estos problemas de manera eficiente.
*/
object Ejercicio2 extends App {
  case class Libro(titulo: String, precio: Double, genero: String)

  val libros = List(
    Libro("El Señor de los Anillos", 15, "Ficción"),
    Libro("Harry Potter", 30, "Ficción"),
    Libro("Cien Años de Soledad", 25, "Ficción"),
    Libro("Mi Casco por almohada", 60, "No Ficción"),
    Libro("El Arte de la Guerra", 18, "No Ficción"),
    Libro("The Good Sheperd", 55, "No Ficción")
  )

  val librosMayorA20 = libros.filter(_.precio > 20)
  println(s"Libros con precio > 20: ")
  librosMayorA20.foreach(println)
  println("--------------------------------------------")

  val generoEspecifico = "Ficción"
  val titulosDeGenero = libros.filter(_.genero == generoEspecifico).map(_.titulo)
  println(s"Títulos de libros en género:")
  titulosDeGenero.foreach(println)
  println("--------------------------------------------")

  val librosExpandidos = libros.flatMap(libro => List(libro.titulo, libro.precio, libro.genero))
  println(s"Lista expandida de libros:")
  librosExpandidos.foreach(println)
  println("--------------------------------------------")

  val librosConDescuento = libros.map { libro =>
    if (libro.precio > 50) Libro(libro.titulo, libro.precio * 0.8, libro.genero)
    else libro
  }
  println(s"Libros con descuento aplicado:")
  librosConDescuento.foreach(println)
  println("--------------------------------------------")

  val clasificadosPorGenero = libros.map { libro =>
    libro.genero match {
      case "Ficción" => ("Ficción", libro)
      case "No Ficción" => ("No Ficción", libro)
      case "Ciencia" => ("Ciencia", libro)
      case _ => ("Otros", libro)
    }
  }

  val ficcion = clasificadosPorGenero.collect { case ("Ficción", libro) => libro.titulo }
  val noFiccion = clasificadosPorGenero.collect { case ("No Ficción", libro) => libro.titulo }
  val ciencia = clasificadosPorGenero.collect { case ("Ciencia", libro) => libro.titulo }
  val otros = clasificadosPorGenero.collect { case ("Otros", libro) => libro.titulo }

  println(s"Ficción: $ficcion")
  println("--------------------------------------------")
  println(s"No Ficción: $noFiccion")
  println("--------------------------------------------")
  println(s"Ciencia: $ciencia")
  println("--------------------------------------------")
  println(s"Otros: $otros")
  println("--------------------------------------------")
}