package me.khabib.chat;

import me.khabib.chat.service.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@SpringBootApplication
public class App {
    // TODO Вынести в сервис
    public static void main1(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("Добро пожаловать. Введите свое имя");
        String username = scanner.nextLine();
        Client client = new Client(username);
        Thread t = new Thread(client::consume);
        t.start();

        while (true) {
            System.out.println("Введите получателя сообщения. all - для всех, exit для выхода");
            String address = scanner.nextLine();
            if ("exit".equals(address)) break;
            System.out.println("Введите сообщение");
            String message = scanner.nextLine();
            client.sendMessage(address, message);
        }
        client.stop();
        t.join();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
