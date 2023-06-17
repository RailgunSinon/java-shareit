package ru.practicum.shareit.exceptionHandlers;

import java.util.Map;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptionHandlers.Entity.ErrorResponse;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StateNotSupportedException;

/* Пришлось немного импровизировать. Я уж не буду спрашивать почему у некоторых тестов странные
Коды ответа, 404 по запрету владельцу возвращать свою вещь - это интересно. Но тут пришлось
Покреативить, чтобы вернуть нужный текст ошибки на сообщение =/ Я перепробовал почти все стандартное
и оно не соответствовало. Это решение работает. Почему и как для меня загадка.
*/
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.error("Предмет или пользователь не обнаружен ");
        return Map.of("Пользователь или предмет не найден ", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException exception) {
        log.error("Не пройдена валидация для создания сущности");
        return Map.of("Ошибка валидации", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRunTimeException(final RuntimeException exception) {
        log.error("Неизвестная ошибка");
        return Map.of("Что-то пошло не так", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleStateNotSupportedException(
        final StateNotSupportedException exception) {
        log.error("Ошибка получения состояния бронирования");
        return new ErrorResponse(400, "Bad Request", exception.getMessage());
    }
}
