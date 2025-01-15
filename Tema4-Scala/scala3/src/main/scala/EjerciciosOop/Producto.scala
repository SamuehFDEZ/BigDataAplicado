package EjerciciosOop

class Producto (val nombre: String, var precio: Int, var cantidadStock: Int) {
  def vender(cantidad: Int): Unit = {
      if (cantidadStock > 0) cantidadStock -= cantidad
      else println(s"¡No hay suficiente stock!")
  }

  def agregarStock(cantidad: Int): Unit = {
    if (cantidadStock > 0) cantidadStock += cantidad
    else println(s"¡Cantidad inválida!")
  }

  def mostrarInfo(): Unit = {
    println(s"Producto: $nombre, Precio: $precio, Cantidad en stock: $cantidadStock")
  }
}

object Producto extends App{
  var producto = new Producto("pc", 1500, 20)
  producto.mostrarInfo()
  producto.vender(10)
  producto.agregarStock(5)
  producto.mostrarInfo()
}