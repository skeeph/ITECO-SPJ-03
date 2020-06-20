package me.khabib.chat.cli.commands.chats;

import me.khabib.chat.cli.Event;
import me.khabib.chat.cli.commands.AbstractCommand;
import me.khabib.chat.service.ChatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class CreateChat extends AbstractCommand {
    private final ChatService chatService;

    public CreateChat(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public String command() {
        return "cc";
    }

    @Override
    public String description() {
        return "Создание чата";
    }

    @Override
    @EventListener(condition = "#event.command eq 'cc'")
    @Transactional
    public void processEvent(Event event) {
        System.out.println("Введите название чата:");
        String chatName = scanner.nextLine();
        if (!StringUtils.isBlank(chatName)) chatService.createChat(chatName);
        else System.out.println("Имя чата не может быть пустым.");
    }
}
