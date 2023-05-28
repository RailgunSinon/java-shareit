package ru.practicum.shareit.item.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

//Пока у нас нет БД, приходится напрямую прокидывать сервис со статикой.
//Разнес логику в контроллерах по аналогии, что ты предложил.
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper mapper;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на добавление предмета");
        itemService.isUserExistsOrException(userId);
        Item item = mapper.convertToEntity(itemService, itemDto, 0, userId);
        itemService.addItem(item);
        return mapper.convertToDto(itemService.getItem(item.getId()));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
        @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на обновление предмета c id " + itemId);
        itemService.isUserExistsOrException(userId);
        Item item = mapper.convertToEntity(itemService, itemDto, itemId, userId);
        itemService.updateItem(item);
        return mapper.convertToDto(itemService.getItem(item.getId()));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        log.debug("Получен запрос на получение предмета c id " + itemId);
        return mapper.convertToDto(itemService.getItem(itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByUserSearch(@RequestParam String text,
        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на получение всех предметов пользователя с id" + userId
            + " и фильтром " + text);
        ArrayList<Item> items = new ArrayList<>(itemService.getItemsByUserSearch(text, userId));
        return mapper.convertToDtoListOfItems(items);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на получение всех предметов пользователя с id" + userId);
        ArrayList<Item> items = new ArrayList<>(itemService.getUserItems(userId));
        return mapper.convertToDtoListOfItems(items);
    }
}