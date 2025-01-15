package AbstractTrait

abstract class FormaDePago {
  def pagar(cantidad: Int): Unit
}

class Tarjeta extends FormaDePago{
  override def pagar(cantidad: Int): Unit =
    println(s"Procesando pago de $cantidad con tarjeta de crédito")
}

class Paypal extends FormaDePago {
  override def pagar(cantidad: Int): Unit =
    println(s"Procesando pago de $cantidad con paypal")
}

class Transferencia extends FormaDePago {
  override def pagar(cantidad: Int): Unit =
    println(s"Procesando pago de $cantidad con transferencia bancaria")
}


object Ejercicio2 extends App {
  val tarjeta = new Tarjeta
  tarjeta.pagar(10)

  val paypal = new Paypal
  paypal.pagar(10)

  val transferencia = new Transferencia
  transferencia.pagar(10)
}
