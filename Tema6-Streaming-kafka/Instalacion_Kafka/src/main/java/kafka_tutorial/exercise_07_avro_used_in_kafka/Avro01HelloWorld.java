package kafka_tutorial.exercise_07_avro_used_in_kafka;

import com.example.Animal;
import com.example.RegistroTransaccion;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Avro01HelloWorld {

    public static void main(String[] args) throws IOException {

        // Crear un objeto Animal
        Animal animal = new Animal();
        animal.setName("Hello Kitty");
        animal.setAge(8);
        animal.setSpecies("Cat");

        // Serializar el objeto
        byte[] serializedAnimal = serialize(animal,animal.getSchema());

        System.out.println("Animal Serializado: ") ;
        printByteArray(serializedAnimal);

        // Deserializar los datos
        Animal deserializedAnimal = (Animal) deserialize(serializedAnimal, animal.getSchema());

        // Imprimir el resultado
        System.out.println("Animal Deserializado: " +
                "Nombre: " + deserializedAnimal.getName() + ", " +
                "Edad: " + deserializedAnimal.getAge() + ", " +
                "Especie: " + deserializedAnimal.getSpecies());

        System.out.println("El animal original es igual al animal deserializado? " + animal.equals(deserializedAnimal));
    }

    // Método para serializar datos
    private static byte[] serialize(GenericRecord object, Schema schema) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(object, encoder);
        encoder.flush();
        outputStream.close();
        return outputStream.toByteArray();
    }



    private static Object deserialize(byte[] data, Schema schema) throws IOException {
        DatumReader<GenericRecord> reader = new SpecificDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        return  reader.read(null, decoder);
    }


    // Método para imprimir la representación en hexadecimal de un array de bytes
    private static void printByteArray(byte[] byteArray) {
        for (byte b : byteArray) {
            System.out.print(String.format("%02X ", b));
        }
        System.out.println();  // Agregar un salto de línea al final
    }
}
