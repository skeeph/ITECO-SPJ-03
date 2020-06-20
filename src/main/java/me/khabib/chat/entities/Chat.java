package me.khabib.chat.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.build.ToStringPlugin;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String link;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "chats")
    @ToStringPlugin.Exclude
    private List<User> users;

    // TODO: 20.06.2020 Переделать связи на LAZY после включения транзакций
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chat")
    private List<Message> messages;
}
