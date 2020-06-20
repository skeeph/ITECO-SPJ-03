package me.khabib.chat.cli.commands.chats;

import me.khabib.chat.cli.Event;
import me.khabib.chat.cli.commands.AbstractCommand;
import me.khabib.chat.service.ChatService;
import me.khabib.chat.service.MessageService;
import me.khabib.chat.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class JoinChat extends AbstractCommand {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    public JoinChat(ChatService chatService, MessageService messageService, UserService userService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public String command() {
        return "jc";
    }

    @Override
    public String description() {
        return "Присоединение к чату";
    }

    @Override
    @Transactional
    @EventListener(condition = "#event.command eq 'jc'")
    public void processEvent(Event event) {
        System.out.println("Введите название чата");
        String chatName = scanner.nextLine();
        if (!StringUtils.isBlank(chatName)) {
            chatService.findChatByName(chatName).ifPresent(chat -> {
                chatService.addUserToChat(chat, userService.getCurrentUser());
                messageService.createGroupChatConsumer(userService.getCurrentUser(), chat);
            });
        } else {
            System.out.println("Имя чата не может быть пустым");
        }
    }
}
