package Ejercicios

object Ejercicio2 extends App{

  def totalImpuestosEnvio(subtotal: Float, tasa: Float, envio: Float) = {
    val precioTotal = subtotal * (1 - (tasa/100)) + envio

    f"- Precio total: $precioTotal%.2f \n" +
      f"- Subtotal $subtotal%.2f \n" +
      f"- Tasa de impuestos $tasa%.2f \n" +
      f"- Envio $envio%.2f"
  }

  println(totalImpuestosEnvio(2,3,4))
}
