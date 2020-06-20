package me.khabib.chat.entities;

import lombok.*;
import net.bytebuddy.build.ToStringPlugin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
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
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat")
    private List<Message> messages;
}
