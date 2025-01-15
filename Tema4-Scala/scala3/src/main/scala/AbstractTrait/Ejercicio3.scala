package AbstractTrait

trait conexionWifi {
  def conectar(electrodomestico: String): String = s"Conectando el $electrodomestico a la red WiFi"
}

trait progAut {
  def prog(tiempo: Int): String = s"Programando electrodoméstico a las $tiempo"
}

class Lavadora extends conexionWifi with progAut 

class Refrigerador extends conexionWifi 

object Ejercicio3 extends App {
  val lavadora = new Lavadora
  lavadora.conectar("boss")
  lavadora.prog(100)
  
  val refrigerador = new Refrigerador
  refrigerador.conectar("hisense")
}
