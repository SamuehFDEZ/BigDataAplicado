package AbstractTrait

abstract class robot(model: String, battery: Int) {
  def modelo(): Unit = println(s"Modelo de robot: $model")
  def bateria(): Unit = println(s"Bateria: $battery")
  def estado(): Unit = println(s"El robot $model tiene $battery% de bateria")
}

trait levantaPeso {
  def levantar(peso: Int): String = s"El robot está levantando $peso kg"
}

trait asistir {
  def asistencia(persona: String): String = s"El robot está asistiendo a $persona"
}


class Obrero extends robot("obrero", 100) with levantaPeso

class Asistente extends robot("asistente", 50) with asistir


object Ejercicio4 extends App{
  val obrero = new Obrero
  obrero.modelo()
  obrero.bateria()
  obrero.estado()
  obrero.levantar(30)
  val asistente = new Asistente
  asistente.modelo()
  asistente.bateria()
  asistente.estado()
  asistente.asistencia("Lucas")
}
