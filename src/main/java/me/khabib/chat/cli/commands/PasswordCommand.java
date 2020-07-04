package me.khabib.chat.cli.commands;

import me.khabib.chat.cli.Event;
import me.khabib.chat.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PasswordCommand extends AbstractCommand {
    private final UserService userService;

    public PasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "passwd";
    }

    @Override
    public String description() {
        return "Установка пароля";
    }

    @Override
    @EventListener(condition = "#event.command eq 'passwd'")
    public void processEvent(Event event) {
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();
        userService.setCurrentUserPassword(password);
    }
}
