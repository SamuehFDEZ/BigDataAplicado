package Ejercicios

object Ejercicio1 extends App{
  def numDivisible(num1: Int, divisor: => Int): String = {
    if(divisor == 0)
      s"El divisor no puede ser 0"
    else
      s"Calculando si número es divisible por el divisor \n $num1 es el valor del párametro y divisor es el valor del parámetro"
  }

  println(numDivisible(2,4))
}
