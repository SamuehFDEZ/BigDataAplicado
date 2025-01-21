package esp.scala3.app
package Map

object Ejercicio2 extends App{
  val listaNums = List("hola", "soy", "lucas", "adoro", "scala")
  val listaMap = listaNums.map(i => i.toUpperCase)
  println(listaMap)
}