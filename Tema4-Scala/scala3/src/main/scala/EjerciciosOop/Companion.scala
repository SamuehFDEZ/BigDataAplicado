package EjerciciosOop

object Libro {
  def crearLibro(titulo: String, autor: String): Libro = new Libro(titulo, autor)
  def crearLibroAnonimo(): Libro = new Libro("Desconocido", "Anónimo")
}
class Libro (val titulo:String, val autor: String){
}

object Companion extends App{
  val libro = Libro.crearLibro("1984", "George Orwell")
  println(s"Titulo: ${libro.titulo}, Autor: ${libro.autor}")
  val libroAnonimo = Libro.crearLibroAnonimo()
  println(s"Titulo: ${libroAnonimo.titulo}, Autor: ${libroAnonimo.autor}")
}