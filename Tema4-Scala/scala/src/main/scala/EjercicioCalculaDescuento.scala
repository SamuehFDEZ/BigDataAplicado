object EjercicioCalculaDescuento extends App {

  def calculaDescuento(precio: Double, descuento: Double = 10.0) = {
    val precioFinal = precio * (1 - descuento / 100)

    if (descuento < 50)
      f"Precio final $precioFinal."
    else
      f"Precio final $precioFinal%2.2f. ¡Descuento muy alto!"
  }

  println(calculaDescuento(50,50))
  println(calculaDescuento(100))
  println(calculaDescuento(200, 20))
  println(calculaDescuento(300, 50))
}