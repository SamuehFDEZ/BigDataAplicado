object Recursividad extends App{
  def factorial (n: Int): Int =
    if (n <= 1) 1
    else{
      val resultado = n * factorial(n - 1)
      resultado
    }

  //println(factorial(50000))

  def otroFactoral(n: Int): Int = {
    def factCalcula(x: Int, acumulador: Int): Int =
      if (x <= 1) acumulador
      else factCalcula(x - 1, x * acumulador)

    factCalcula(n, 1)
  }

  println(otroFactoral(50000))
}
