package ru.practicum.shareit.user.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        log.info("Создание нового пользователя");
        userRepository.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        log.info("Изменение пользователя с id " + user.getId());
        userRepository.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(int userId) {
        log.info("Получение пользователя с id " + userId);
        return userRepository.getUserById(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        log.info("Удаление пользователя с id " + userId);
        userRepository.deleteUserById(userId);
    }

    @Override
    public boolean isUserExists(int userId) {
        log.info("Запрос существования пользователя с id " + userId);
        return userRepository.isUserExists(userId);
    }
}
