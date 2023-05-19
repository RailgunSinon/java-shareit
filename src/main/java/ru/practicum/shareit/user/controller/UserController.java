package ru.practicum.shareit.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private int idCounter = 1;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Получен запрос на добавление пользователя");
        User user = convertToEntity(userDto,idCounter++);
        userService.addUser(user);
        return convertToDto(userService.getUserById(user.getId()));
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserDto userDto,
        @RequestParam(required = false) int userId) {
        log.debug("Получен запрос на обновление пользователя");
        User user = convertToEntity(userDto,userId);
        userService.updateUser(user);
        return convertToDto(userService.getUserById(user.getId()));
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        log.debug("Получен запрос на получение всех пользователей");
        ArrayList<User> users = new ArrayList<>(userService.getAllUsers());
        ArrayList<UserDto> userDtos = new ArrayList<>();
        for(User user : users){
            userDtos.add(convertToDto(user));
        }
        return userDtos;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.debug("Получен запрос на получение пользователя по id");
        return convertToDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        log.debug("Получен запрос на удаление пользователя по id");
        userService.deleteUserById(userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException exception) {
        log.error("Не пройдена валидация для создания пользователя");
        return Map.of("Ошибка валидации", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.error("Пользователь не обнаружен");
        return Map.of("Пользователь не найден", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRunTimeException(final RuntimeException exception) {
        log.error("Неизвестная ошибка");
        return Map.of("Что-то пошло не так", exception.getMessage());
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    private User convertToEntity(UserDto userDto, int userId) {
        User user = modelMapper.map(userDto, User.class);
        user.setId(userId);
        return user;
    }

}
