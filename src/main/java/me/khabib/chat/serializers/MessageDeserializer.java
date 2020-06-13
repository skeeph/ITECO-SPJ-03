package me.khabib.chat.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.khabib.chat.dto.Message;
import org.apache.kafka.common.serialization.Deserializer;

public class MessageDeserializer implements Deserializer<Message> {
    @Override
    public Message deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        Message message;
        try {
            message = mapper.readValue(data, Message.class);
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO 13.06.2020 murtuzaaliev: Нормальная обработка исключений
        }
        return message;
    }
}
