package EjerciciosOop

sealed class Vehiculo(val TipoVehiculo: String = "General") {
  def conducir(): Unit = println("Conduciendo vehículo")
}

class coche(override val TipoVehiculo: String) extends Vehiculo{
  override def conducir(): Unit =
    super.conducir()
    println("Conduciendo coche")
}


object Herencia extends App {
  val coche = new coche("Sedán")
  println(coche.TipoVehiculo)
  coche.conducir()
}
