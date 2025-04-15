package kafka_tutorial.exercise_02_basic_producer_consumer;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("auto.offset.reset", "earliest");
        properties.put("group.id", "AppIdentifer005");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        Gson gson = new Gson();

        kafkaConsumer.subscribe(Arrays.asList("myTopic"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    Order order = gson.fromJson(record.value(), Order.class);
                    System.out.printf("Order Received: ID=%s, Customer=%s, Total=%.2f\n",
                            order.orderId, order.customerName, order.total);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }
}
