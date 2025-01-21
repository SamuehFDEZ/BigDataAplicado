package esp.scala3.app
package Map

object Ejercicio3 extends App {
  val listaNombresCompletos = List("Samuel Arteaga", "Lucas Tarazona", "Roberto Ripoll")

  val listaReversa = listaNombresCompletos.map(i => i.split(" ")(1) + ", "+ i.split(" ")(0))
  println(listaReversa)
}
