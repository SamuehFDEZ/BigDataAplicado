package EjerciciosOop

class Mascota (val nombre:String, var actividadFavorita:String){
  def cambiarActividad(nuevaActividad: String): Unit = {
    this.actividadFavorita = nuevaActividad
  }

  def mostrarInfo(): Unit = {
    println(s"Mascota: $nombre, Actividad favorita $actividadFavorita")
  }
}

object Mascota extends App{
  val mascota = new Mascota("Firulais", "dormir")
  mascota.cambiarActividad("correr")
  println(mascota.mostrarInfo())
}
