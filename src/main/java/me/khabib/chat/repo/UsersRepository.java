package me.khabib.chat.repo;

import me.khabib.chat.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
