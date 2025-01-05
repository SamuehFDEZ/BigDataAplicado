object Ejercicios extends App {
  // Ejercicio 1
//  def saludar(nombre: String, edad: Int) = {
//    s"Hola, mi nombre es $nombre y tengo $edad años"
//  }
//
//  println(saludar("Samuel", 19))
//  // Ejercicio 2
//
//  def factorial(n: Int): Int = {
//    if (n == 0) 1
//    else n * factorial(n - 1)
//  }
//
//  println(factorial(5))

   //Ejercicio 3
  // gemini
   def esPrimo(num: Int, divisor: Int = 2): Boolean = {
     if (num <= 1) false // Números <= 1 no son primos
     else if (divisor * divisor > num) true // No se encontraron divisores hasta la raíz cuadrada, es primo
     else if (num % divisor == 0) false // Si es divisible, no es primo
     else esPrimo(num, divisor + 1) // Sigue con el siguiente divisor
   }

  println(esPrimo(2))  // true
//  println(esPrimo(7))  // true
//  println(esPrimo(42)) // false
//
//  println(esPrimo(2))
//  println(esPrimo(7))
//  println(esPrimo(42))
}