package Functions.Anonimas

import scala.annotation.tailrec

@tailrec
def nVeces(f: Int => Int, n: Int, x: Int): Int = {
  if (n <= 0) x
  else nVeces(f, n - 1, f(x))
}

object Anonimas extends App{

  val unoMas = (x: Int) => x + 1
  println(nVeces(unoMas, 10, 1))
}


