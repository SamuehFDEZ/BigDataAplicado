package esp.scala3.app
package patternMatching

object patternMatching extends App {
  sealed trait MyList[+A]
  case object Empty extends MyList[Nothing]
  case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]
  
  // 1. constantes
  val x: Any = "Scala"
  val constants = x match {
    case 1 => "un número"
    case "Scala" => "SCALA"
    case true => "La verdad"
    case patternMatching =>  "Objeto Singleton"  //Evalúa si x coincide con el objeto singleton AllThePatterns. Patrón que busca una referencia específica.
  }
  
  //2 - match 
  //2.1 Comodín
  val matchAnything: Unit = x match {
    case _ => //el comodín
  }
  //2.2 variable
  val matchAVariable = x match {
    case something => s"Yo he encontrado $something" //es una variable de captura. Coincide con cualquier valor de x.
  }

  //3- Tuplas
  val aTuple = (1,2)
  val matchATuple = aTuple match {
    case (1,1) => //comprueba exactamente la tupla (1,1)
    case (something, 2) => s"Yo he encontrado $something" //Coincide con cualquier tupla cuya segunda posición sea 2 y asigna el valor de la primera posición a la variable something
  }

  val nestedTuple = (1, (2, 3))
  val matchANestedTuple = nestedTuple match {
    case (_, (2, v)) => // Coincide con una tupla cuyo segundo elemento es otra tupla (2,v). Se ignora el primer valor con _
  }

  //4. case classes - constructor pattern
  val aList: List[Int] = Cons (1, Cons(2, Empty))
  val matchAList = aList match {
    case Empty => //Coincide si aList es una lista vacía
    case Cons(head, Cons(subhead, subtail)) =>  //Coincide con una lista no vacía de tipo Cons y 
    //descompone la lista para extraer el primer elemento (head), el segundo elemento (suhead) y el resto subtail
  }

  //5. List patterns
  val aStandarList = List (1,2,3,42)
  val standardListMatching = aStandarList match {
    case List (1,_,_,_) => //Coincide con la lista exactamente de 4 elementos donde el primero es 1
    case List (1, _*) => // Coincide con listas que comienzan con 1 y tienen cualquier longitud.
    case 1 :: List (_) => //Usa el operarador infijo :: para coincidir con listas que comienzan con 1 y tienen exactamente un elemento más 
    case List (1,2,3) :+ 42 => //Coincide con listas que terminan en 42
  }

  //6. types específicos
  val unknown : Any = 2
  val unknownMatch = unknown match {
    case list: List[Int] => //comprueba si unknown es una lista de enteros
    case _ => // comodín
  }

  //7. name binding
  val nameBindingMatch = aList match {
    case notEmptyList @ Cons (_,_) => //Vincula toda la lista no vacía al nombre notEmptyList mientras verifica que coincide con Cons(_,_)
    case Cons(1, rest @ Cons(2, _)) => // rest @ Cons (2,_) vincula el resto de la lista al nombre rest mientras coincide con el patrón.
  }

  //8. multi patterns
  val multpattern = aList match {
    case Empty | Cons (0,_ ) => //utiliza | para combinar patrones.
  }

  //9. if 
  val secondElemento = aList match {
    case Cons(_, Cons(specialElemento,_ )) if specialElemento % 2 == 0 => //Añade una condición adicional al patrón que coincide solo si el segundo elemento es par
  }
}