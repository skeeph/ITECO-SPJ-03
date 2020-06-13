package me.khabib.chat.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.khabib.chat.dto.Message;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class MessageSerializer implements Serializer<Message> {
    @Override
    public byte[] serialize(String topic, Message message) {
        ObjectMapper objectMapper = new ObjectMapper();


        try {
            return objectMapper.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); //TODO 13.06.2020 murtuzaaliev: Нормальная обработка исключений
        }
    }
}
