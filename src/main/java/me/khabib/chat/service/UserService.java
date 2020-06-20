package me.khabib.chat.service;

import lombok.Getter;
import me.khabib.chat.entities.Role;
import me.khabib.chat.entities.User;
import me.khabib.chat.repo.UsersRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final MessageService messageService;
    @Getter
    private User currentUser;

    public UserService(UsersRepository usersRepository, MessageService messageService) {
        this.usersRepository = usersRepository;
        this.messageService = messageService;
    }

    /**
     * Возвращает есть ли залогиненные пользователь в чате
     *
     * @return true - если есть
     */
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Авторизация пользователя
     *
     * @param username имя пользоватея
     */
    @Transactional
    public void login(String username) {
        if (StringUtils.isBlank(username)) {
            System.out.println("Имя пользователя не может быть пустым");
            return;
        }
        currentUser = usersRepository.findByUsername(username).orElseGet(() -> usersRepository.save(new User(username)));
        messageService.subscribePrivateMessages(currentUser);
        currentUser.getChats().forEach(chat -> messageService.createGroupChatConsumer(currentUser, chat));
    }

    public void saveUser(User user) {
        usersRepository.save(user);
    }

    /**
     * @return true - если текущий пользователь - админ
     */
    public boolean isCurrentUserAdmin() {
        return currentUser.getRole() == Role.ADMIN;
    }

    public void makeCurrentUserAdmin() {
        currentUser.setRole(Role.ADMIN);
        usersRepository.save(currentUser);
        System.out.println("Помни: с большой силой приходит и большая ответственность");
    }

    public Optional<User> findUser(String userName) {
        return usersRepository.findByUsername(userName);
    }
}
