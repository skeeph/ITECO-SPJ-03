package me.khabib.chat.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private long id;

    private String text;

    private Long time;

    // TODO: 20.06.2020 Переделать связи на LAZY после включения транзакций
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User author;


    // TODO: 20.06.2020 Переделать связи на LAZY после включения транзакций
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="chat_id")
    private Chat chat;
}
