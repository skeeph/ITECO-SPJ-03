package me.khabib.chat.configs;

import me.khabib.chat.serializers.MessageDeserializer;
import me.khabib.chat.serializers.MessageSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaConfig {
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String BROADCAST_TOPIC = "messages.broadcast";
    
    public static Properties getKafkaProps(String username){
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
}
