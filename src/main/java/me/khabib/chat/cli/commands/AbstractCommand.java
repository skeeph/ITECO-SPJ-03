package me.khabib.chat.cli.commands;

import me.khabib.chat.cli.Event;

import java.util.Scanner;

public abstract class AbstractCommand {
    protected final Scanner scanner = new Scanner(System.in);

    public abstract String command();

    public abstract String description();

    public abstract void processEvent(Event event);
}
