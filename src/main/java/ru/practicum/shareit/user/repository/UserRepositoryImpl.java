package ru.practicum.shareit.user.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с таким id не был найден");
        }
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с таким id не был найден");
        }
        return users.get(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с таким id не был найден");
        }
        users.remove(userId);
    }
}
