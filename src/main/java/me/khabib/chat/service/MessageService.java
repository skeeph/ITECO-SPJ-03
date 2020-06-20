package me.khabib.chat.service;

import me.khabib.chat.dto.Message;
import me.khabib.chat.entities.Chat;
import me.khabib.chat.entities.MessageDirection;
import me.khabib.chat.entities.User;
import me.khabib.chat.repo.ChatRepository;
import me.khabib.chat.repo.MessagesRepository;
import me.khabib.chat.repo.UsersRepository;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory;
    private final UsersRepository usersRepository;
    private final ChatRepository chatRepository;
    private final MessagesRepository messagesRepository;

    public MessageService(KafkaTemplate<String, Message> kafkaTemplate, ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory, UsersRepository usersRepository, ChatRepository chatRepository, MessagesRepository messagesRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
        this.usersRepository = usersRepository;
        this.chatRepository = chatRepository;
        this.messagesRepository = messagesRepository;
    }

    /**
     * Подписка на личные сообщения пользователя
     *
     * @param user пользователь
     */
    public void subscribePrivateMessages(User user) {
        ConcurrentMessageListenerContainer<String, Message> container = kafkaListenerContainerFactory.createContainer(user.getUsername());
        container.getContainerProperties().setGroupId(user.getUsername());
        container.setupMessageListener((MessageListener<String, Message>) x -> {
            System.out.printf("[PRIVATE] %s: %s", x.value().getAuthor(), x.value().getMessage());
            saveMessage(x.value().getMessage(), x.value().getAuthor(), "", MessageDirection.IN);
        });

        container.start();
    }

    private boolean unsubscribeMessage(Message message) {
        return usersRepository.findByUsername(message.getAuthor()).isPresent() && message.getMessage().startsWith("EXTREMINATUS.");
    }

    /**
     * Подписка пользователя на чат
     *
     * @param user пользователь
     * @param chat чат
     */
    public void createGroupChatConsumer(User user, Chat chat) {
        ConcurrentMessageListenerContainer<String, Message> container = kafkaListenerContainerFactory.createContainer(chat.getName());
        container.getContainerProperties().setGroupId(user.getUsername());
        container.setupMessageListener((MessageListener<String, Message>) x -> {
            if (unsubscribeMessage(x.value())) {
                if (x.value().getMessage().split("\\.")[1].equals(user.getUsername()))
                    container.stop();
            } else {
                System.out.printf("[%s] %s: %s",
                        chat.getName(), x.value().getAuthor(), x.value().getMessage());
                saveMessage(x.value().getMessage(), x.value().getAuthor(), chat.getName(), MessageDirection.IN);
            }

        });
        container.start();
    }

    /**
     * Отправка сообщения.
     *
     * @param user      Автор сообщений
     * @param recipient получатель(пользователь или чат)
     * @param text      сообщение
     */
    public void sendMessage(User user, String recipient, String text) {
        Message message = new Message(user.getUsername(), text, recipient);
        kafkaTemplate.send(recipient, message);
        saveMessage(text, user.getUsername(), recipient, MessageDirection.OUT);
    }

    private void saveMessage(String text, String authorName, String chat, MessageDirection direction) {
        me.khabib.chat.entities.Message entity = new me.khabib.chat.entities.Message();
        entity.setText(text);
        entity.setTime(System.currentTimeMillis());
        entity.setDirection(direction);
        usersRepository.findByUsername(authorName).ifPresent(entity::setAuthor);
        chatRepository.findChatByName(chat).ifPresent(entity::setChat);
        messagesRepository.save(entity);
    }

    public List<me.khabib.chat.entities.Message> getChatHistory(Chat chat) {
        return messagesRepository.findAllByChatOrderByTimeAsc(chat);
    }
}
