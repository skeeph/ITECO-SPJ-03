package me.khabib.chat.main;

import me.khabib.chat.service.Client;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать. Введите свое имя");
        String username = scanner.nextLine();
        Client client = new Client(username);
        Thread t = new Thread(client::consume);
        t.start();

        String message = "";
        while (!"exit".equals(message)) {
            message = scanner.nextLine();
            client.sendMessage(message);
        }
        t.interrupt();
    }
}