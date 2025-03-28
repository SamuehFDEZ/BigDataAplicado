package esp.scala3.app
package Opciones

import scala.util.Random

object Scala_Options extends App {
    //Option para representar la presencia o ausencia de valores
    val config: Map [String, String] = Map (
        "host" -> "176.45.36.1",
        "port" -> "80"
    )
    class Connection {
        def connect = "Conectado" //conectado algún servidor
    }
    object Connection {
      val random = new Random (System.nanoTime())
      def apply (host: String, port:String): Option [Connection] = 
         if (random.nextBoolean()) Some (new Connection)
         else None
    }
    val host = config.get ("host")
    val port = config.get ("port")
    /* 
       if (h != null)
        if (p != null)
            return Connection.apply (h ,p)
       return null
     */
    val connection = host.flatMap (h => port.flatMap (p => Connection.apply (h, p)))
    /* 
       if (c != null)
         return c.connect
        return null
     */
    val connectionStatus = connection.map(c => c.connect)
    // if (connectionStatus == null) println (None) else print (Some(connectionStatus.get))
    println(connectionStatus)
    /* 
       if (status 1= null)
       println(status)
     */
    connectionStatus.foreach(println)

   /* es el mismo código?*/
    config.get ("host")
        .flatMap(host => config.get("port")
            .flatMap(port => Connection(host, port))
            .map(connection => connection.connect))
        .foreach(println)

    //for-comprehensions
    val forConnectionStatus = for {
        host <- config.get ("host")
        port <- config.get ("port")
        connection <- Connection (host,port)
    } yield connection.connect
    forConnectionStatus.foreach(println)
}