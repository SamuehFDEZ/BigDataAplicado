package Ejercicios

import scala.annotation.tailrec

object Ejercicio3 extends App{
  @tailrec
  def buscaNum(lista: List[Int], num: Int): Boolean = {
    if(lista.isEmpty) false
    else lista.head == num || buscaNum(lista.tail, num)
  }

  println(buscaNum(List(1,2,3,4),3))
}
