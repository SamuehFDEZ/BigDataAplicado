object Expresiones extends App{
  val x = 1 + 2
  println(x)

  println(2 + 3 * 4)

  println(1 == x)

  println(!(1 == x))

  var aVariable = 2
  aVariable += 3
  println(aVariable)

  // Instrucciones (DO) vs Expresiones (VALUE)
  // Instrucciones (algo que haga la computadora) vs
  // Expresiones algo que tiene un valor
  val aCondicion = true
  // IF como expresion no como instruccion
  val aCondicionValor = if(aCondicion) 5 else 3
  println(aCondicionValor)
  println(if(aCondicion) 5 else 3)
  println(1 + 3)

  val h: Int = 4
  if (h < 5) {
    print("Hola")
  }
  else{
    print("hola")
  }


  val aCodBloque = {
    val t = 2
    val z = t + 2
    if (z > 2) "Hola" else "adios"
  }

  println(aCodBloque)
}
