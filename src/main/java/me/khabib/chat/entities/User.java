package me.khabib.chat.entities;

import lombok.*;
import net.bytebuddy.build.ToStringPlugin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "user_chats",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    @ToStringPlugin.Exclude
    private Set<Chat> chats = new HashSet<>();

    public User(String username) {
        this.username = username;
        this.role = Role.USER;
    }
}
