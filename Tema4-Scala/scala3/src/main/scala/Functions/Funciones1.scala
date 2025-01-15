package Functions

object Funciones1 {

  val funcion = new ((String) => String) {
    override def apply(string: String): String = string + string
  }

  def main(args: Array[String]): Unit = {
    println(funcion("cadena"))
  }
}

