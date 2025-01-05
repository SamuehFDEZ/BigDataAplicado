object StringOps extends App{
  val str: String = "Hola, yo estoy aprendiendo Scala"

  // devuelve el caracter en la posicion 2 (l)
  println(str.charAt(2))

  // devuelve un nuevo string desde la poiscion inicial hasta la final en los parametros
  println(str.substring(7, 11))

  // divide la cadena en espacios y lo convierte a lista
  println(str.split(" ").toList)

  // encuentra en la cadena por parametro, si no está da error
  println(str.startsWith("Hola"))

  // remplaza los espacios por guines
  println(str.replace(" ", "-"))

  // convierte la cadena a minusculas
  println(str.toLowerCase())

  // devuelve la longitud del string
  println(str.length)

  val aNumberString = "2"
  val aNumber = aNumberString.toInt

  println("a" +: aNumberString :+ "z")

  // invierte la cadena
  println(str.reverse)

  // obtiene los dos primeros caracteres de la cadena
  println(str.take(2))

  // string interpolation
  val name = "David"
  val edad = 12
  val presentacion = s"Hola mi nombre es $name y tengo $edad años"
  val otraPresentacion = s"Hola mi nombre es $name y cumpliré ${edad + 1} el año que viene"
  println(otraPresentacion)

  // format interpolation
  // $nombre%s indica que el valor de la variable nombre debe tratarse como una cadena de texto (string)
  // %f indica que es de tipo float
  // 2.2 el primer 2 indica el numero de caracteres a usar, el segundo 2 el numero de decimales
  val velocidad = 1.2f
  val nombre = "Roberto"
  println(f"$nombre%s puede comer $velocidad%2.2f bocadillos por minuto")

  // raw-interpolation no tiene en cuenta los \n etc

  println("Esto es una nueva \n linea")
  println(raw"Esto es una nueva \n linea")
  val linea = "Esto es una nueva \n linea"
  println(raw"$linea")

}
