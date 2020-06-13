package me.khabib.chat.service;

import me.khabib.chat.dto.Message;

public class Client {
    private final String username;
    private final KafkaClient kafka;
    private boolean stopped = false;


    public Client(String username) {
        this.username = username;
        this.kafka = new KafkaClient(username);

    }

    public void consume() {
        while (!this.stopped) {
            var messages = kafka.getMessages();
            for (Message message : messages) {
                System.out.println(String.format("%s=>%s", message.getAuthor(), message.getMessage()));
            }
        }
        kafka.getConsumer().close();
    }

    public void sendMessage(String address, String text) {
        Message message = new Message(this.username, text, address);
        kafka.send(message);
    }

    public void stop() {
        this.stopped = true;
    }
}
