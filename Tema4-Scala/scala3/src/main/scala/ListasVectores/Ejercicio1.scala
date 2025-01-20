package esp.scala3.app
package ListasVectores

object Ejercicio1 extends App{
  // 1: Array
  var lista = List(1,2,3)
  println(lista.mkString("List(", ", ", ")"))
  lista = lista :+ 4
  println(lista.mkString("List(", ", ", ")"))

  // 2: Lista
  val listaPuntuaciones = List(2,4,5,10,3,2)
  println(listaPuntuaciones(1)) // posicion 1 = 4

  //3
  var lista2 = List(2,4,1,6,10,20)
  lista2 = lista2 :+ 3
  println(lista2.sorted())

  // 4
  var array2 = Array(1,2,3)
  array2(2) = 10
  println(array2.mkString("Array(", ", ", ")"))
}
