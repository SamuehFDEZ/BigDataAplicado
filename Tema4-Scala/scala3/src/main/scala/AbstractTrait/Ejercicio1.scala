package AbstractTrait

abstract class Dispositivo {
  def encender(): Unit
  def apagar(): Unit
}

class Telefono extends Dispositivo {
  override def encender(): Unit = println("El telefono está encendido")
  def llamar(numero: Int): Unit = println(s"Llamando al número $numero")
  override def apagar(): Unit = println("El telefono está apagado")

}

class Tablet extends Dispositivo {
  override def encender(): Unit = println("La tablet está encendido")
  def navegar(url: String): Unit = println(s"Navegando a $url")
  override def apagar(): Unit = println("La tablet está apagado")

}


object Ejercicio1 extends App {
  val telefono = new Telefono
  telefono.encender()
  telefono.llamar(123456789)
  telefono.apagar()

  val tablet = new Tablet
  tablet.encender()
  tablet.navegar("https://www.google.com")
  tablet.apagar()
}
