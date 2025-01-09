package Excepciones


object Excepciones {
  def sumar(x: Int, y: Int): Unit = {
    try {
      val resultado = x + y
      if(resultado > Int.MaxValue) throw new ArithmeticException("El numero supera el limite del int")
      else println(resultado)
    }
    catch {
      case e: ArithmeticException => println("El numero supera el limite del int")
    }

  }

  def restar(x: Int, y: Int): Unit = {
    try {
      val resultado = x - y
      if(resultado > Int.MinValue) throw new ArithmeticException("El numero esta por debajo del limite del int")
      else println(resultado)
    }
    catch {
      case e: ArithmeticException => println("El numero esta por debajo del limite del int")
    }
  }

  def multiplicar(x: Int, y: Int): Unit = {
    try {
      val resultado = x * y
      if(resultado < Int.MinValue || resultado > Int.MaxValue) throw new ArithmeticException("El numero esta por debajo del limite del int o por encima del limite")
      else println(resultado)
    }
    catch {
      case e: ArithmeticException => println("El numero esta por debajo del limite del int o por encima del limite")
    }
  }

  def dividir(x: Int, y: Int): Unit = {
    try {
      val resultado = x / y
      if(y == 0 ) throw new Exception("No se puede dividir entre 0")
      else println(resultado)
    }
    catch {
      case e: Exception => println("No se puede dividir entre 0")
    }
  }

  def main(args: Array[String]): Unit = {
    sumar(100000000, 3)
    restar(2, 999999999)
    multiplicar(999999999, 3)
    dividir(2, 0)
  }
}