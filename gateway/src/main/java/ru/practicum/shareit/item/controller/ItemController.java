package ru.practicum.shareit.item.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto itemDto,
        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на добавление предмета со стороны клиента");
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
        @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на обновление предмета c id " + itemId + "со стороны клиента");
        return itemClient.patchItem(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long itemId) {
        log.debug("Получен запрос на удаление предмета по id со стороны клиента");
        itemClient.deleteItem(itemId);
        return ResponseEntity.ok().body("Удалено");
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос на получение предмета c id " + itemId + "со стороны клиента");
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByUserSearch(@RequestParam String text,
        @RequestHeader("X-Sharer-User-Id") Long userId,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Получен запрос на получение всех предметов пользователя с id" + userId
            + " и фильтром " + text + "со стороны клиента");
        return itemClient.getItemsByUserSearch(userId, text, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
        @RequestParam(defaultValue = "0") @Min(0) Integer from,
        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Получен запрос на получение всех предметов пользователя с id" + userId + "со "
            + "стороны клиента");
        return itemClient.getAllUserItems(userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
        @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.debug("Получен запрос на добавление комментария от пользователя " + userId + "со "
            + "стороны клиента");
        return itemClient.createItemComment(commentDto, itemId, userId);
    }
}