package me.khabib.chat.service;

import me.khabib.chat.dto.Message;
import me.khabib.chat.serializers.MessageDeserializer;
import me.khabib.chat.serializers.MessageSerializer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
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
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static me.khabib.chat.configs.KafkaConfig.BOOTSTRAP_SERVERS;
import static me.khabib.chat.configs.KafkaConfig.BROADCAST_TOPIC;

public class Client {
    private final String topic;
    private final String username;
    private final Consumer<String, Message> consumer;
    private final Producer<String, Message> producer;
    private boolean stopped = false;


    public Client(String username) {
        this.username = username;
        this.topic = "messages.pv." + username;
        this.consumer = createConsumer();
        this.producer = createProducer();
    }

    private void createTopics() {
        AdminClient adminClient = AdminClient.create(getKafkaProps());
        NewTopic privateTopic = new NewTopic(topic, 1, (short) 1); 
        NewTopic broadcastTopic = new NewTopic(BROADCAST_TOPIC, 1, (short) 1);
        adminClient.createTopics(List.of(privateTopic, broadcastTopic));
        adminClient.close();

    }

    private Consumer<String, Message> createConsumer() {
        // Create the consumer using props.
        createTopics();
        final Consumer<String, Message> consumer = new KafkaConsumer<>(getKafkaProps());

        // Subscribe to the topic.
        consumer.subscribe(List.of(BROADCAST_TOPIC, topic));
        return consumer;
    }

    private Producer<String, Message> createProducer() {
        Properties props = getKafkaProps();
        return new KafkaProducer<>(props);
    }

    private Properties getKafkaProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer" + username);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_" + username);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
        return props;
    }

    public void consume() {
        try {
            while (!this.stopped) {
                ConsumerRecords<String, Message> records = this.consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, Message> record : records) {
                    Message message = record.value();
                    if (!this.username.equals(message.getAuthor())){
                        System.out.println(String.format("%s=>%s", message.getAuthor(), message.getMessage()));
                    }
                }
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    public void sendMessage(String address, String message) {
        String topic;
        if ("all".equals(address))
            topic = BROADCAST_TOPIC;
        else topic = "messages.pv." + address;
        final ProducerRecord<String, Message> record =
                new ProducerRecord<>(topic, new Message(this.username, message));
        producer.send(record, (metadata, exception) -> {
            if (metadata != null) {
                //TODO 14.06.2020 murtuzaaliev: Логгирование!
//                System.out.printf("sent record(key=%s value=%s) " +
//                                "meta(partition=%d, offset=%d)\n",
//                        record.key(), record.value(), metadata.partition(),
//                        metadata.offset());
            } else {
                exception.printStackTrace();
            }
        });
        producer.flush();
    }

    public void stop() {
        this.stopped = true;
    }
}
