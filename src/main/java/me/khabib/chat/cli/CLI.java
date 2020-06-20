package me.khabib.chat.cli;

import me.khabib.chat.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CLI implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private static final String EXIT = "exit";
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public CLI(UserService userService, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    @Override
    public void run(String... args) {
        System.out.println("Введите ваше имя:");
        while (!userService.isUserLoggedIn()){
            userService.login(scanner.nextLine());
        }
        String command = "";
        System.out.println("Введите комманду. help - для получения помощи");
        while (!EXIT.equals(command)){
            command = scanner.nextLine();
            publisher.publishEvent(new Event(command));
        }

    }
}
