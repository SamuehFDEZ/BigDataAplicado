package EjerciciosOop

class Sistema(val concierto: String, var entradasDisp: Int = 50) {

  // Reservar una cantidad específica de entradas
  def reservar(cantidad: Int): Unit = {
    if (cantidad <= 0) println("La cantidad a reservar debe ser mayor a 0.")
    else if (cantidad > entradasDisp) println(s"No se pueden reservar $cantidad entradas. Solo quedan $entradasDisp disponibles.")
    else
      entradasDisp -= cantidad
      println(s"Se han reservado $cantidad entradas para el $concierto. Quedan $entradasDisp entradas disponibles.")
  }

  // Cancelar una cantidad específica de reservas
  def cancelarReserva(cantidad: Int): Unit = {
    if (cantidad <= 0 && cantidad > entradasDisp) println("La cantidad a cancelar debe ser mayor a 0 y no ha de superar la cantidad disponible")
    else
      entradasDisp += cantidad
      println(s"Se han cancelado $cantidad entradas para el $concierto. Ahora hay $entradasDisp entradas disponibles.")

  }

  // Mostrar el estado actualizado de las reservas
  def estadoReservas(): Unit = {
    println(s"Entradas disponibles para el $concierto: $entradasDisp")
  }
}

object Ejercicio_7 extends App {
  val sistema = new Sistema("Concierto de Rock")

  sistema.reservar(10)
  sistema.estadoReservas()

  sistema.cancelarReserva(30)
  sistema.estadoReservas()
          
  sistema.reservar(60)
  sistema.estadoReservas()
}
