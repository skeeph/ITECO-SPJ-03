package me.khabib.chat.cli.commands.chats;

import me.khabib.chat.cli.Event;
import me.khabib.chat.cli.commands.AbstractCommand;
import me.khabib.chat.entities.Chat;
import me.khabib.chat.service.ChatService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ListChats extends AbstractCommand {
    private final ChatService chatService;

    public ListChats(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public String command() {
        return "lc";
    }

    @Override
    public String description() {
        return "Список чатов";
    }

    @Override
    @Transactional
    @EventListener(condition = "#event.command eq 'lc'")
    public void processEvent(Event event) {
        for (Chat chat : chatService.getAllChats()) {
            System.out.printf("%s - %s\n", chat.getName(), chat.getLink());
        }
    }
}
