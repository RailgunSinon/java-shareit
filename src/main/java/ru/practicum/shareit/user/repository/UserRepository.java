package ru.practicum.shareit.user.repository;

import java.util.List;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    void addUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int userId);

    void deleteUserById(int userId);
}
