package esp.scala3.app
package ClaseMap

object Ejercicio2 extends App{
  val listaNums = List("hola", "soy", "lucas", "adoro", "scala")
  val listaMap = listaNums.map(i => i.toUpperCase)
  println(listaMap)
}