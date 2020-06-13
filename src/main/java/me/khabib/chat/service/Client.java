package me.khabib.chat.service;

import me.khabib.chat.dto.Message;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static me.khabib.chat.configs.KafkaConfig.BOOTSTRAP_SERVERS;
import static me.khabib.chat.configs.KafkaConfig.BROADCAST_TOPIC;

public class Client {
    private final String topic;
    private final String username;
    private final Consumer<String, String> consumer;
    private final Producer<String, String> producer;


    public Client(String username) {
        this.username = username;
        this.topic = "messages:pv:" + username;
        this.consumer = createConsumer();
        this.producer = createProducer();
    }

    private Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "consumer_" + username);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        // Create the consumer using props.
        final Consumer<String, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(List.of(BROADCAST_TOPIC));
        return consumer;
    }

    private Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer" + username);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public void consume() {
        try {
            while (!Thread.interrupted()) {
                ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records)
                    System.out.println(record.offset() + ":" + record.value());
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    public void sendMessage(String message) {
        final ProducerRecord<String, String> record =
                new ProducerRecord<>(BROADCAST_TOPIC, message);
        producer.send(record, (metadata, exception) -> {
            if (metadata != null) {
                System.out.printf("sent record(key=%s value=%s) " +
                                "meta(partition=%d, offset=%d)\n",
                        record.key(), record.value(), metadata.partition(),
                        metadata.offset());
            } else {
                exception.printStackTrace();
            }
        });
        producer.flush();

    }
}
