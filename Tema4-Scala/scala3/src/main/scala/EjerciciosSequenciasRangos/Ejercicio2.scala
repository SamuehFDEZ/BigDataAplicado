package esp.scala3.app
package EjerciciosSequenciasRangos

object Ejercicio2 {
  val rangoClases: Seq[Int] = 1 until 10

  def main(args: Array[String]): Unit = {
    (8 to 17).foreach(x => print(s"$x "))
    println()
    (8 to 17).foreach(x => print(s" Clase en la hora $x"))
    println()
    println(s"Número total de clases: ${rangoClases.size}")
    (8 to 17 by 2).foreach(x => print(s" Descanso: $x"))
  }
}
