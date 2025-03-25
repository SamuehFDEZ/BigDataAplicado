package kafka_tutorial.exercise_07_avro_used_in_kafka;
import com.example.Animal;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AvroKafkaConsumer {
    public static void main(String[] args) {
        // Configuración del consumidor Kafka
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "animal-group-id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"); // Especifica la URL del registro de esquemas Avro
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        try (final KafkaConsumer<String, Animal> consumer2 = new KafkaConsumer<>(props)) {
            consumer2.subscribe(Collections.singletonList("topic-animales"));

            while (true) {
                final ConsumerRecords<String, Animal> records = consumer2.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, Animal> record : records) {
                    final String key = record.key();
                    final Animal value = record.value();
                    System.out.printf("key = %s, value = %s%n", key, value);
                }
            }

        }


    }
}

