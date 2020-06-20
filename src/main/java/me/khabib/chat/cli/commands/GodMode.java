package me.khabib.chat.cli.commands;

import me.khabib.chat.cli.Event;
import me.khabib.chat.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class GodMode extends AbstractCommand {
    private final UserService userService;

    public GodMode(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "IDDQD";
    }

    @Override
    public String description() {
        return "Пользователь становится админом";
    }

    @Override
    @EventListener(condition = "#event.command eq 'IDDQD'")
    @Transactional
    public void processEvent(Event event) {
        userService.makeCurrentUserAdmin();
    }
}
