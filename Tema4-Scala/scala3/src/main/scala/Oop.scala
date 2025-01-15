
//constructor
class Persona (nombre: String, val edad: Int) {
  //cuerpo
  val x = 2 //son campos
  println (1 + 3) //expresiones
  //método --//overloading
  def saluda(nombre: String): Unit = println(s"${this.nombre} dice: Hola, $nombre")
  def saluda(): Unit = println (s"Hola, Yo soy $nombre")
  //def saluda(): Int = 43 --> En este caso el compilado sí se confude

  //multiple constructores
  def this(nombre: String) = this(nombre,0)
  def this() = this ("Juan Carlos")
}

//los parámetros de la clase no son CAMPOS si no tiene escrito delante la palabra reservada "val"

object Oop extends App{


  val persona = new Persona ("Carlos", 25)
  println(persona.edad)
  println(persona.x)
  persona.saluda("Daniel")
  persona.saluda()


}