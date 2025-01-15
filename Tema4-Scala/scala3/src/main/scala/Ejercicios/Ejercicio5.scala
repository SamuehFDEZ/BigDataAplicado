package Ejercicios

import scala.annotation.tailrec

object Ejercicio5 extends App {

  def cuentaCaracter(cadena: String, caracter: Char): Int = {
    @tailrec
    def cuentaNumCaracter(cadena: String, caracter: Char, contador: Int): Int = {
      if (cadena.isEmpty) contador
      else {
        val nuevoContador = if (cadena.head == caracter) contador + 1 else contador
        cuentaNumCaracter(cadena.tail, caracter, nuevoContador)
      }
    }
    cuentaNumCaracter(cadena, caracter, 0)
  }

  println(cuentaCaracter("hoiooooooolllllaaa", 'a'))
}
