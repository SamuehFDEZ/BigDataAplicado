package Functions

val funcion = new(Int => (Int => Int)) {
  override def apply(v1: Int): Int => Int = (v2: Int) => v1 + v2
}

object Funciones2 {

  def main(args: Array[String]): Unit = {
    val prefuncion = funcion(2)

    val resultado = prefuncion(3)

    println(s"Resultado: $resultado")
  }
}
