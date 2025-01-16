package esp.scala3.app
package HighOrderFunctionsEjercicios

object Ejercicio3 {
  def main(args: Array[String]): Unit = {
    val generaMensaje: String => String => String => String = (saludo: String) => (nombre: String) => (mensaje: String)
    => saludo + " " + nombre + " " + mensaje
    // Parte 1
    println(generaMensaje("Hola")("Samuel")("¿Te gusta scala?"))

    val saludoCliente = generaMensaje("Estimado Cliente")
    // Parte 2
    val carlos = saludoCliente("Carlos")("Su pedido ha sido enviado con éxito")
    println(carlos)

    // Parte 3
    println(generaMensaje("Hola")("María")("Gracias por registrarse en nuestra plataformma"))
  }
}
