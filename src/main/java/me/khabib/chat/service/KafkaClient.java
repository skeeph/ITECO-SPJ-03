package me.khabib.chat.service;

import lombok.Getter;
import me.khabib.chat.configs.KafkaConfig;
import me.khabib.chat.dto.Message;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static me.khabib.chat.configs.KafkaConfig.BROADCAST_TOPIC;

public class KafkaClient {

    @Getter
    private final Consumer<String, Message> consumer;
    @Getter
    private final Producer<String, Message> producer;
    private final String topic;
    private final String username;

    public KafkaClient(String username) {
        this.username = username;
        Properties props = KafkaConfig.getKafkaProps(username);
        this.topic = "messages.pv." + username;
        createTopics(props);
        this.consumer = createConsumer(props);
        this.producer = createProducer(props);
    }

    private void createTopics(Properties props) {
        AdminClient adminClient = AdminClient.create(props);
        NewTopic privateTopic = new NewTopic(topic, 1, (short) 1);
        NewTopic broadcastTopic = new NewTopic(BROADCAST_TOPIC, 1, (short) 1);
        adminClient.createTopics(List.of(privateTopic, broadcastTopic));
        adminClient.close();
    }

    private Consumer<String, Message> createConsumer(Properties props) {
        final Consumer<String, Message> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(BROADCAST_TOPIC, topic));
        return consumer;
    }

    private Producer<String, Message> createProducer(Properties props) {
        return new KafkaProducer<>(props);
    }

    public void send(Message message) {
        String topic = "all".equals(message.getAddress()) ? BROADCAST_TOPIC : "messages.pv." + message.getAddress();
        final ProducerRecord<String, Message> record =
                new ProducerRecord<>(topic, message);
        producer.send(record);
        producer.flush();
    }
    

    public List<Message> getMessages() {
        ConsumerRecords<String, Message> records = consumer.poll(Duration.ofSeconds(1));
        return StreamSupport
                .stream(records.spliterator(),false)
                .map(ConsumerRecord::value)
                .filter(x->!x.getAuthor().equals(this.username))
                .collect(Collectors.toList());
    }
}
