package esp.scala3.app
package HighOrderFunctionsEjercicios

object Ejercicio2 {
  def main(args: Array[String]): Unit = {
    val superConcatenar: String => (String => String) = (x: String) => (y: String) => x +" "+ y

    val prefHola = superConcatenar("Hola")
    println(prefHola("Caracola"))
  }
}
