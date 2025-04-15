package kafka_tutorial.exercise_02_basic_producer_consumer;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Producer {

    public static void main(String[] args){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        Gson gson = new Gson();

        try{
            for(int i = 0; i < 1000; i++){
                Thread.sleep(1000);
                String orderId = UUID.randomUUID().toString();
                List<String> items = Arrays.asList("itemA", "itemB", "itemC");
                Order order = new Order(orderId, "Cliente " + i, items, 99.99 + i);
                String jsonOrder = gson.toJson(order);
                System.out.println("Enviando: " + jsonOrder);
                kafkaProducer.send(new ProducerRecord<>("myTopic", orderId, jsonOrder));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            kafkaProducer.close();
        }
    }
}
