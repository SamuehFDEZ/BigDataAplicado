object Funciones extends App{
  def aFuncion1(a: String, b: Int): String = {
    a + " " + b
  }

  println(aFuncion1("Hola", 3))

  def aParametrosFuncion(): Int = 42
  println(aParametrosFuncion())
  //println(aParametrosFuncion)

  // cuando necesites bucles, usa recursividad

  def aRecursivaFuncion1(aString: String, n: Int): String = {
    if (n == 1) aString
    else aString + aRecursivaFuncion1(aString, n -1)
  }

  println(aRecursivaFuncion1("Hola", 3))

  def principalFuncion(n: Int): Int = {
    def secundariaFuncion(a: Int, b:Int):Int = a + b
    secundariaFuncion(n, n-1)
  }

  println(principalFuncion(5))


}
