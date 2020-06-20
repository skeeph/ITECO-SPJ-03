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
public class SendToChatCommand extends AbstractCommand {
    private final UserService userService;
    private final MessageService messageService;
    private final ChatService chatService;


    public SendToChatCommand(UserService userService, MessageService messageService, ChatService chatService) {
        this.userService = userService;
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @Override
    public String command() {
        return "cm";
    }

    @Override
    public String description() {
        return "Отправка сообщения в чат";
    }

    @Override
    @EventListener(condition = "#event.command eq 'cm'")
    @Transactional
    public void processEvent(Event event) {
        System.out.println("Введите имя чата.");
        String chatName = scanner.nextLine();
        if (!StringUtils.isBlank(chatName)) {
            String invite = "Введите сообщение \n--history для того чтобы показать историю чата.\n--exit чтобы завершить команду";
            String adminHint = userService.isCurrentUserAdmin() ? "\n--del чтобы удалить пользователя из чата" : "";

            System.out.println(invite + adminHint);
            String message = scanner.nextLine();
            switch (message) {
                case "--exit":
                    return;
                case "--del":
                    sendUnsubscribeMessage(chatName);

                    break;
                case "--history":
                    chatService.printChatHistory(chatName);
                    break;
                default:
                    messageService.sendMessage(userService.getCurrentUser(), chatName, message);
            }
        } else {
            System.out.println("Получатель не может быть пустым");
        }
    }

    private void sendUnsubscribeMessage(String chatName) {
        if (!userService.isCurrentUserAdmin()) {
            System.out.println("ТЫ НЕ ПРОЙДЕШЬ!");
            return;
        }
        System.out.println("Кого удалить из чата?");
        String target = scanner.nextLine();
        if (!target.isBlank()) {
            chatService.deleteUserFromChat(chatName, target);
        }else {
            System.out.println("неверное имя пользователя.");
        }
    }
}
