package esp.scala3.app
package ListasVectores

import scala.util.Random

object Vectorizar extends App {

  val vector: Vector[Int] = Vector(1, 2, 3)
  println(vector)

  //vector vs lists
  val maxRuns = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt)
      System.nanoTime - currentTime
    }
    times.sum * 1.0 / maxRuns
  }

  val numberLists = (1 to maxCapacity).toList
  val numberVector = (1 to maxCapacity).toVector

  println(getWriteTime(numberLists))
  println(getWriteTime(numberVector))
}
/* Dado que las listas y los vectores se comportan de forma diferente internamente, el tiempo de updated
* en los vectores es más eficientes que en las listas, al menos en este caso, porque las listas en otros
* casos serán más eficientes que los vectores */