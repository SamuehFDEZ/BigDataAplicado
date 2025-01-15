package CaseClases

object EjercicioEnum {
  enum Color {
    case ROJO, AZUL, AMARILLO
  }
  
  def main(args: Array[String]): Unit = {
    println(Color.ROJO)
    println(Color.AZUL)
    println(Color.AMARILLO)
  }
}