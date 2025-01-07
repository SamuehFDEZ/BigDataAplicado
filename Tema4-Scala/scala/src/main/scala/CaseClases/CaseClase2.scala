package CaseClases

case class Operacion(a: Int, b: Int) {
  def calcularOperacion(operacion: String): Int =
    operacion match {
      case "sumar" => a + b
      case "resta" => a - b
      case "multiplicar" => a * b
    }
}

object CaseClase2 extends App {
  val operacion = Operacion(2, 4)
  println(operacion.calcularOperacion("sumar"))
  println(operacion.calcularOperacion("resta"))
  println(operacion.calcularOperacion("multiplicar"))
}
