# AVRO

## Objectives

 1) Practice to use AVRO within Kafka.

### Requirements

 * IntelliJ IDE
 * Maven and JDK installed in IntelliJ IDE

## Run

### Ejercicio 1
Ejecuta el programa y verifica que puedes enviar y recibir mensajes de tipo Animal (un POJO Java creado automáticamente
a tiempo de compilación con el plugin maven de AVRO).

Hint: Debugea los programa, especialmente el Consumer.

### Ejercicio 2
Modifica el programa creando/usando otro POJO creado por ti con otro fichero avsc. Ejecútalo y verifica que todo va ok.


### Ejercicio 3
Ejecuta un consumidor Kafka en la consola y verifica que los mensajes AVRO comprimidos no son legibles por un ser humano.
```commandline
docker-compose exec broker kafka-console-consumer --topic topic-animales2 --from-beginning --bootstrap-server localhost:9092
```

Ejcuta el siguiente consumidor Kafka en una consola y verifica que ahora los mensajes AVRO si son legibles por un ser humano.
````commandline
docker-compose exec schema-registry  kafka-avro-console-consumer --bootstrap-server broker:29092 --topic topic-animales2 --property schema.registry.url=http://schema-registry:8081 --from-beginning
````