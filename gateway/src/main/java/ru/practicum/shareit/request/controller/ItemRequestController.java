package ru.practicum.shareit.request.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@AllArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на добавление нового запроса на предмет со стороны клиента");
        return itemRequestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequest(@PathVariable final Long id,
        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на получения запроса на предмет с id " + id + "со стороны "
            + "клиента");
        return itemRequestClient.getItemRequest(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на получения запросов на предмет от пользователя с id " + userId
            + "со стороны клиента");
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersRequests(
        @RequestHeader("X-Sharer-User-Id") Long userId,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Получен запрос на получения запросов на предмет других пользователей со "
            + "стороны клиента");
        return itemRequestClient.getOtherRequests(userId, from, size);
    }

}
