package Ejercicios

object Ejercicio4 extends App{
  def reverseList(list: List[Int]): List[Int] = {
    list match {
      case Nil => list
      case (primerElemento :: restoDeLista) => reverseList(restoDeLista) ::: List(primerElemento)
    }
  }
  println(reverseList(List(1,2,3,4,5,6,7,8,9,10)))
}
