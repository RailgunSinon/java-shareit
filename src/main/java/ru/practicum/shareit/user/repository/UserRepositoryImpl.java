package ru.practicum.shareit.user.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;


@Component
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Integer, User> users = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public void addUser(User user) {
        if (emailExists(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        }
        if (!users.containsKey(user.getId())) {
            user.setId(idCounter++);
            users.put(user.getId(), user);
        }
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с таким id не был найден");
        }
        if (emailExists(user.getEmail(), user)) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
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

    @Override
    public boolean isUserExists(int userId) {
        if (users.containsKey(userId)) {
            return true;
        }
        return false;
    }

    private boolean emailExists(String email) {
        for (User user : users.values()) {
            if (user.getEmail().toLowerCase().equals(email.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean emailExists(String email, User userForUpdate) {
        HashMap<Integer, User> updateUsers = new HashMap<>(users);
        updateUsers.remove(userForUpdate.getId());
        for (User user : updateUsers.values()) {
            if (user.getEmail().toLowerCase().equals(email.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
