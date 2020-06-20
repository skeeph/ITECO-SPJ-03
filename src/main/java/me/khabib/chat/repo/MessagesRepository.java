package me.khabib.chat.repo;

import me.khabib.chat.entities.Chat;
import me.khabib.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatOrderByTimeAsc(Chat chat);
}
