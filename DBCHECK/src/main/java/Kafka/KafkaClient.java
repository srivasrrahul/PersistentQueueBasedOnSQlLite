package Kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by rasrivastava on 10/26/15.
 */

public class KafkaClient {
    void test() {
        Properties props = new Properties();

        props.put("metadata.broker.list", "10.25.160.103:9092,10.24.16.154:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);


        Producer<String, String> producer = new Producer<>(config);

        Random rnd = new Random();

        long runtime = new Date().getTime();

        String ip = "192.168.2." + rnd.nextInt(255);

        String msg = runtime + "www.example.com" + ip;

        KeyedMessage<String, String> data = new KeyedMessage<>("consumer.engg", ip, msg);

        producer.send(data);


    }

    public static void main(String[] args) throws InterruptedException {
        KafkaClient kafkaClient = new KafkaClient();
        for (;;) {
            kafkaClient.test();
            Thread.sleep(2000);
        }
    }
}
