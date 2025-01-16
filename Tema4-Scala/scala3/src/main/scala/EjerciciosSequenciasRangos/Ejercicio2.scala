package esp.scala3.app
package EjerciciosSequenciasRangos

object Ejercicio2 {

  def main(args: Array[String]): Unit = {
    (8 to 17).foreach(x => print(s"$x "))
    println()
    (8 to 17).foreach(x => print(s" Clase en la hora $x"))
    println()
    println(s"Número total de clases: ${(1 to 10).size}")
    (8 to 17 by 2).foreach(x => print(s" Descanso: $x"))
  }
}
