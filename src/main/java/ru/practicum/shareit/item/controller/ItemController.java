package ru.practicum.shareit.item.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

//Пока у нас нет БД, приходится напрямую прокидывать сервис со статикой.
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper = new ModelMapper();
    private static int idCounter = 1;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на добавление предмета");
        if (!userService.isUserExists(userId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        Item item = convertToEntity(itemDto, idCounter++, userId);
        itemService.addItem(item);
        return convertToDto(itemService.getItem(item.getId()));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
        @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на обновление предмета c id " + itemId);
        if (!userService.isUserExists(userId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        Item item = convertToEntity(itemDto, itemId, userId);
        itemService.updateItem(item);
        return convertToDto(itemService.getItem(itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        log.debug("Получен запрос на получение предмета c id " + itemId);
        return convertToDto(itemService.getItem(itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByUserSearch(@RequestParam String text,
        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на получение всех предметов пользователя с id" + userId
            + " и фильтром " + text);
        ArrayList<Item> items = new ArrayList<>(itemService.getItemsByUserSearch(text, userId));
        return convertToDtoListOfItems(items);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.debug("Получен запрос на получение всех предметов пользователя с id" + userId);
        ArrayList<Item> items = new ArrayList<>(itemService.getUserItems(userId));
        return convertToDtoListOfItems(items);
    }

    private ItemDto convertToDto(Item item) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        return itemDto;
    }

    private List<ItemDto> convertToDtoListOfItems(List<Item> items) {
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(convertToDto(item));
        }
        return itemDtos;
    }

    private Item convertToEntity(ItemDto itemDto, int itemId, int userId) {
        Item item = modelMapper.map(itemDto, Item.class);
        item.setId(itemId);
        item.setUserId(userId);
        Item oldItem;

        if (itemService.isItemExists(itemId)) {
            oldItem = itemService.getItem(itemId);
            if (oldItem.getUserId() != userId) {
                throw new NotFoundException("Пользователь не найден!");
            }
            if (item.getName() == null) {
                item.setName(oldItem.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(oldItem.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(oldItem.getAvailable());
            }
        }
        return item;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException exception) {
        log.error("Не пройдена валидация для создания предмета");
        return Map.of("Ошибка валидации", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.error("Предмет не обнаружен");
        return Map.of("Пользователь не найден", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRunTimeException(final RuntimeException exception) {
        log.error("Неизвестная ошибка");
        return Map.of("Что-то пошло не так", exception.getMessage());
    }
}