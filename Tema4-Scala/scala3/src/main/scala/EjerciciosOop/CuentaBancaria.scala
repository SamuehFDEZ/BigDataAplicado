package EjerciciosOop

class CuentaBancaria(val numeroDeCuenta: String, var saldoActual: Double) {
  def depositar(cantidad: Double):Unit = {
      if (cantidad > 0) {
        saldoActual += cantidad
        println(s"Se han depositado $cantidad en la cuenta $numeroDeCuenta. Saldo actual: $saldoActual")
      } else if(cantidad == 0 || cantidad < 0){
        println(s"No se puede depositar una cantidad negativa o cero")
      }
  }

  def retirar(cantidad: Double):Unit = {
    if ((cantidad < 0) || (cantidad > saldoActual)) {
      println(s"No se puede retirar una cantidad negativa o cero")
    } else {
      saldoActual -= cantidad
      if (cantidad > saldoActual) {
        println(s"No hay suficiente saldo para retirar $cantidad. Saldo actual: $saldoActual")
      }
      if (cantidad < saldoActual) {
        println(s"Se han retirado $cantidad de la cuenta $numeroDeCuenta. Saldo actual: $saldoActual")

      }
    }
  }
  def mostrarInfo(): Unit = {
    println(s"Cuenta: $numeroDeCuenta, Saldo actual: $saldoActual")
  }
}

object CuentaBancaria extends App{
  val cuenta = new CuentaBancaria("6728462", 2000)
  cuenta.depositar(500)
  cuenta.retirar(200)
  cuenta.retirar(1500)
  cuenta.mostrarInfo()
}
