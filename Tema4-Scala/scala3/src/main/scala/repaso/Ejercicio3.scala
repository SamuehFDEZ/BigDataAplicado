package esp.scala3.app
package repaso

object Ejercicio3 extends App {
  case class Asistente(nombre: String, edad: Int, correo: String)

  val listaAsistentes = List(
    Asistente("Samuel", 20, "samu@gmail.com"),
    Asistente("Lucas", 23, "lucas@gmail.com"),
    Asistente("Roberto", 22, "rober@gmail.com"),
    Asistente("Álvaro", 27, "alvaro@gmail.com"),
    Asistente("David", 22, "")

  )

  val listaMayoresDeEdad = listaAsistentes
    .filter(_.edad >= 18)
    .map(asistente => "Nombre: " + asistente.nombre.toUpperCase() 
      + ", Correo: " + asistente.correo)

  listaMayoresDeEdad.foreach(println)
}