package CaseClases

case class Libro(titulo: String, autor: String, anyoPublicacion: Int)

object CaseClase extends App {
  val libro = Libro("1984", "Anónimo", 1986)
  println(libro)
}