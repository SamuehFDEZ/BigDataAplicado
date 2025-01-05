object EsPrimo extends App{
  /**
   * Función que calcula si un numero es primo o no
   * @param num número entero
   * @param divisor número estatico con valor 2, ya que todos los numeros son divisibles entre 1
   * @return true o false si es primo o no
   */
  def esPrimo(num: Int, divisor: Int = 2): Boolean = {
    if (num <= 1) false // si el numero es menor o igual a 1 no es primo
    else if (divisor * divisor > num) true // si el cuadrado del divisor es mayor que el número, entonces no se
    // encontraron divisores y por lo tanto el número es primo
    else if (num % divisor == 0) false
    else esPrimo(num, divisor + 1) // sino, se vuelve a ejecutar la funcion sumandole al divisor +1 hasta encontrar
    // todas las posibilidade de que ese numero sea primo o no
  }

  println(esPrimo(2))
  println(esPrimo(7))
  println(esPrimo(42))
  println(esPrimo(643287487))
  println(esPrimo(46))
  println(esPrimo(49087))
}
