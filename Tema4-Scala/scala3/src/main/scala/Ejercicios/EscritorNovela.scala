package Ejercicios

class Escritor(val nombre: String, val primerApellido: String, val anyoAutor: Int) {
  def nombreCompleto(): String = {
    s"Mi nombre completo es $nombre $primerApellido y nací en $anyoAutor"
  }
}

class Novela(val nombre: String, val anyoRealizacion: Int, val autor: Escritor) {
  def edadAutor(): Int = {
    anyoRealizacion - autor.anyoAutor
  }

  def escritoPor(otroAutor: Escritor): Escritor = {
    if (this.autor == otroAutor) {
      this.autor
    } else {
      otroAutor
    }
  }
}

object EscritorNovela extends App {
  val escritor = new Escritor("Samuel", "Arteaga", 1935)
  val novela = new Novela("El señor de los anillos", 1954, escritor)

  println(escritor.nombreCompleto())
  println(s"La edad del autor al escribir la novela fue: ${novela.edadAutor()} años")
  println(s"La novela fue escrita por: ${novela.escritoPor(escritor).nombreCompleto()}")
}
