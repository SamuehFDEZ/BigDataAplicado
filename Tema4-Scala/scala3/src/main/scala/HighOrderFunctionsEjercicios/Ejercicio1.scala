package esp.scala3.app
package HighOrderFunctionsEjercicios

object Ejercicio1 {

  def main(args: Array[String]): Unit = {
    val superMult: Int => (Int => Int) = (x: Int) => (y: Int) => x * y
    
    println(superMult(5)(7))
  }
}
