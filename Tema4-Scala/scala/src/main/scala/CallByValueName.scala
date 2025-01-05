object CallByValueName extends App{

  def LlamadaPorValor(x: Long): Unit = {
    println(s"Por valor $x")
    println(s"Por valor $x")
  }

  def LlamadaPorNombre(x: => Long): Unit = {
    println(s"Por nombre $x")
    println(s"Por nombre $x")
  }

  LlamadaPorValor(System.nanoTime())
  LlamadaPorNombre(System.nanoTime())

  def Infinito(): Int = 1 + Infinito()

  def imprimirPrimer(x: Int, y: => Int): Unit = println(x)

  imprimirPrimer(Infinito(), 34)
  imprimirPrimer(34, Infinito())
}


