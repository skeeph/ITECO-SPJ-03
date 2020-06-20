package me.khabib.chat.service;

import me.khabib.chat.entities.Chat;
import me.khabib.chat.entities.User;
import me.khabib.chat.repo.ChatRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageService messageService;

    public ChatService(ChatRepository chatRepository, UserService userService, MessageService messageService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
        this.messageService = messageService;
    }

    /**
     * Создание чата и подписка текущего пользователя на сообщения этого чата
     *
     * @param chatName имя чата
     */
    public void createChat(String chatName) {
        Chat chat = new Chat();
        chat.setName(chatName);
        chat.setLink(chatName);
        chatRepository.save(chat);
        userService.getCurrentUser().getChats().add(chat);
        userService.saveUser(userService.getCurrentUser());
        messageService.createGroupChatConsumer(userService.getCurrentUser(), chat);
    }

    /**
     * @return Список всех чатов
     */
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Optional<Chat> findChatByName(String chatName) {
        return chatRepository.findChatByName(chatName);
    }

    public void addUserToChat(Chat chat, User user) {
        user.getChats().add(chat);
        userService.saveUser(user);
    }

    /**
     * Печать истории сообщения для чата
     *
     * @param chatName имя чата
     */
    @Transactional
    public void printChatHistory(String chatName) {
        chatRepository.findChatByName(chatName).ifPresent(chat ->
                messageService.getChatHistory(chat).forEach(message ->
                        System.out.printf("[%s] %s %s\n", message.getChat().getName(), message.getAuthor().getUsername(), message.getText())
                )
        );
    }

    /**
     * Удаление пользователя из чата
     * @param chatName имя чата
     * @param username имя пользователя
     */
    public void deleteUserFromChat(String chatName, String username) {
        Optional<Chat> chat = chatRepository.findChatByName(chatName);
        Optional<User> user = userService.findUser(username);
        if (chat.isEmpty() || user.isEmpty()) {
            return;
        }
        boolean removed = user.get().getChats().removeIf(x -> x.getName().equals(chatName));
        System.out.println(removed ? "User " + username + " deleted." : "no such user in chat");
        if (removed) {
            messageService.sendMessage(userService.getCurrentUser(), chatName, "EXTREMINATUS."+username);
            userService.saveUser(user.get());
        }

    }
}
