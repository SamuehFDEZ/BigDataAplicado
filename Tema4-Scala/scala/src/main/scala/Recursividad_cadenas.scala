import scala.annotation.tailrec

object Recursividad_cadenas extends App{
  // funcion que concatena una cadena n veces
  @tailrec
  def cadenaConcatenada(cadena: String, numVeces: Int, acumulador: String): String = {
      if (numVeces <= 0) acumulador
      else cadenaConcatenada(cadena, numVeces - 1, acumulador)
  }
  println(cadenaConcatenada("hola", 3, ""))
}