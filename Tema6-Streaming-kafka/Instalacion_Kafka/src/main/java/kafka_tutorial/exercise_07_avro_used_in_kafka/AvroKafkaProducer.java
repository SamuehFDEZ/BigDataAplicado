package kafka_tutorial.exercise_07_avro_used_in_kafka;

import com.github.javafaker.Faker;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import com.example.Animal;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class AvroKafkaProducer {
    public static void main(String[] args) throws IOException {
        // Configuración del productor Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", "http://localhost:8081"); // Especifica la URL del registro de esquemas Avro

        // Crear un productor Kafka
        Producer<String, Animal> producer = new KafkaProducer<>(props);

        // Crear un registro Avro y enviarlo al tópico Kafka
        Animal animal = createAnimal("Hellow Kitty", "Blanco", 8,"Gato",true);
        ProducerRecord<String, Animal> kafkaRecord = new ProducerRecord<>("topic-animales", "key", animal);
        producer.send(kafkaRecord);

        // Cerrar el productor
        producer.close();
    }

    private static Animal createAnimal(String nombre, String color, int edad, String especie, boolean tieneCola) {
        return com.example.Animal.newBuilder()
                .setName(nombre)
                .setAge(edad)
                .setSpecies(especie)
                .setHasTail(tieneCola)
                .setColor(color)
                .build();
    }
}

