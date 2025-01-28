package esp.scala3.app
package Opciones

object Option extends App {
    val sistema: Map[String, Map[String, String]] = Map(
      "1" -> Map("nombre" -> "Samuel", "saldo" -> "1300"),
      "2" -> Map("nombre" -> "Roberto", "saldo" -> "1300"),
      "3" -> Map("nombre" -> "Lucas")
    )

    def obtenerUsuario(id: String): Unit = {
      sistema.get(id) match {
        case Some(infoUsuario) =>
          val nombre = infoUsuario.getOrElse("nombre", "Desconocido")
          infoUsuario.get("saldo") match {
            case Some(saldo) =>
              val saldoNumerico = saldo.toDouble
              val nuevoSaldo = saldoNumerico * 1.10
              println(s"(Id: $id, Nombre: $nombre, Nuevo Saldo: $nuevoSaldo)")
            case None =>
              println(s"(Id: $id, Nombre: $nombre) ---> OJO: NO TIENE SALDO")
          }
        case None =>
      }
    }

    obtenerUsuario("1")
    obtenerUsuario("2")
    obtenerUsuario("3")
    obtenerUsuario("4")
}