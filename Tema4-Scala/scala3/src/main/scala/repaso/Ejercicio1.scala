package esp.scala3.app
package repaso

/* Ejercicio 1
 Gestión de tienda de productos electrónicos donde cada producto tiene un nombre, un precio y una categoría.
 Realizaremos las siguientes operaciones:
    Filtrar los productos con un precio mayor a 100.
    Obtener los nombres de los productos de una categoría específica.
    Expandir la lista de productos en función de su categoría utilizando flatMap.
    Aplicar una oferta de descuento de 20% a los productos que tengan un precio mayor a 200.
    Clasificar los productos según su categoría utilizando match.
 */
object Ejercicio1 extends App  {
  case class Producto(nombre: String, precio: Double, categoria: String)

  val productos = List(
    Producto("Microondas", 300, "Electrónica"),
    Producto("Tele", 80, "Electrónica"),
    Producto("Portatil", 1200, "Computadoras"),
    Producto("Teclado", 40, "Computadoras"),
    Producto("Móvil", 150, "Electrónica"),
    Producto("Ratón", 20, "Computadoras")
  )

  val productosMayorA100 = productos.filter(_.precio > 100)
  println(s"Productos con precio mayor a 100: $productosMayorA100")

  val productosCategoria = productos.filter(_.categoria == "Electrónica").map(_.nombre)
  println(s"Nombres de productos en categoría Electrónica: $productosCategoria")

  val listaExpandida = productos.flatMap(producto => List(producto.nombre, producto.precio, producto.categoria))
  println(s"Lista expandida de productos: $listaExpandida")

  val descuento = productos.map { p =>
    if (p.precio > 200) Producto(p.nombre, p.precio * 0.8, p.categoria)
    else p
  }
  println(s"Productos con descuento: $descuento")

  val clasificadosPorCategoria = productos.map { producto =>
    producto.categoria match {
      case "Electrónica" => ("Electrónica", producto.nombre)
      case "Computadoras" => ("Computadoras", producto.nombre)
      case _ => ("Otras", producto.nombre)
    }
  }

  val electronica = clasificadosPorCategoria.collect { case ("Electrónica", p) => p }
  val computadoras = clasificadosPorCategoria.collect { case ("Computadoras", p) => p }
  val otras = clasificadosPorCategoria.collect { case ("Otras", p) => p }
  
  println(s"Electrónica: $electronica")
  println(s"Computadoras: $computadoras")
  println(s"Otras: $otras")
}