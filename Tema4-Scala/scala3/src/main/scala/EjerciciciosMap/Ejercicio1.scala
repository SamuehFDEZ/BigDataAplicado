package esp.scala3.app
package EjerciciciosMap

object Ejercicio1 extends App {

    val alumnos = List("Samuel", "Lucas", "Roberto")
    val asignaturas = List("PIA", "BDA", "SAA", "MIA", "SBD")

    val combinaciones = for {
        alumno <- alumnos
        asignatura <- asignaturas
    } yield s"$alumno está inscrito en $asignatura"

    combinaciones.foreach(println)
}