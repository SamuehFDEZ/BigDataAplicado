package esp.scala3.app
package EjerciciosSequenciasRangos

object Ejercicio1 {

  val sequ = Seq(8,15,20,18,22,16,19,21)

  def main(args: Array[String]): Unit = {
    println(sequ)
    println(sequ.max)
    println(sequ.min)
    println(sequ.sum / sequ.length)
    println(sequ.sorted)
    println(sequ ++ Seq(30))
    println(sequ.filter(_ >= 20))
  }
}
