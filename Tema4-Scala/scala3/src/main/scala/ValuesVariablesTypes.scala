object ValuesVariablesTypes extends App{
  val x: Int = 42
  print(x)

  // Otros tipos
  val aString: String = "Hola"
  val otroString = "Adios"

  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val aShort: Short = 4613 // 2 bytes
  val aLong: Long = 74389573453868L // 8 bytes
  val aFloat: Float = 2.0f
  val aDouble: Double = 3.14

  // Variables
  var aVariable: Int = 4
  aVariable = 5

  // val = const de javascript
  val y: Int = 42
  println(y)

  // type mismatch
  //val t: Int = "Hola"

  val bBoolean: Boolean = false
  val aCaracter: Char = 'a'
  println(aCaracter)
  val aEntero: Int = x
  println(aEntero)

  // type mismatch integer
  //val aShort: Short = 23454646

  var z: Int = 1
  z = 1
  z +=1
  print(z)
}
