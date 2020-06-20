package me.khabib.chat.cli.commands;

import me.khabib.chat.cli.Event;
import me.khabib.chat.service.MessageService;
import me.khabib.chat.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MessageCommand extends AbstractCommand{
    private final MessageService messageService;
    private final UserService userService;

    public MessageCommand(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public String command() {
        return "dm";
    }

    @Override
    public String description() {
        return "Отправка личного сообщения сообщений";
    }

    @Override
    @EventListener(condition = "#event.command eq 'dm'")
    public void processEvent(Event event) {
        System.out.println("Введите имя получателя");
        String recipient = scanner.nextLine();
        if (!StringUtils.isBlank(recipient)){
            System.out.println("Введите сообщение");
            String message = scanner.nextLine();
            if (!StringUtils.isBlank(message))
                messageService.sendMessage(userService.getCurrentUser(), recipient, message);
        }else {
            System.out.println("Получатель не может быть пустым");
        }
    }
}
