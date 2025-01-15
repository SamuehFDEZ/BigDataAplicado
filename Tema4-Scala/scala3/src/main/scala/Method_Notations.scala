object Method_Notations extends App {
  class Persona(val nombre: String, peliculaFavorita: String) {
    def meGusta(pelicula: String): Boolean = pelicula == peliculaFavorita

    def salirCon(persona: Persona): String = s"${this.nombre} sale con ${persona.nombre}"

    def +(persona: Persona): String = s"${this.nombre} no le gusta ${persona.nombre}" //método llamado + es válido

    def unary_! : String = s"$nombre, que es esto?" //debe haber un espacio entre unary_! y los dos puntos

    def apply(): String = s"Hola, mi nombre es $nombre y me gusta $peliculaFavorita"

  }

  val maria = new Persona("Maria", "Los vengadores")
  //NOTACIONES INFIJOS = OPERADORES
  println(maria.meGusta("Los vengadores")) //True
  println(maria meGusta "Los vengadores") //True


  // operadores en Scala
  val pedro = new Persona("Pedro", "Tiburón")
  println(maria.salirCon(pedro))
  println(maria salirCon pedro)

  println(maria + pedro)
  println(maria.+(pedro))


  //TODOS LOS OPERADORES SON MÉTODOS
  println(1 + 2)
  println(1.+(2))


  //NOTACION PREFIJOS
  println(!maria)
  println(maria.unary_!)

  //NOTACION más PREFIJOS
  val x = -1 //equivalente con 1.unary_-
  val y = 1.unary_-
  val z = 2.unary_+
  val j = 3.unary_~
  //unary_prefix solo con - + ~ !

  println(y) // valor negativo
  println(z) //valor positivo
  println(j) //El operador ~ tiene sentido en operaciones bit a bit,
  //útil en manipulación de bits, como en programación de bajo nivel
  // o en el desarrollo de aplicaciones que requieren optimización binaria.
  //Este método se encuentra definido para tipos como Int y Long en Scala.

  //apply
  println(maria.apply())
  println(maria()) //equivalente
}