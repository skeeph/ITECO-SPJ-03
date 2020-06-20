package me.khabib.chat.cli.commands;

import me.khabib.chat.cli.Event;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelpCommand extends AbstractCommand {
    private final List<? extends AbstractCommand> commandsList;

    public HelpCommand(List<? extends AbstractCommand> commandsList) {
        this.commandsList = commandsList;
    }

    @Override
    public String command() {
        return "help";
    }

    @Override
    public String description() {
        return "Shows help";
    }

    @EventListener(condition = "#event.command eq 'help'")
    public void processEvent(Event event) {
        for (AbstractCommand command : commandsList) {
            System.out.println(command.command() + ": " + command.description());
        }
    }
}
