package ru.practicum.shareit.item.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public void addItem(Item item) {
        log.info("Создание нового предмета");
        itemRepository.addItem(item);
    }

    @Override
    public void updateItem(Item item) {
        log.info("Обновление предмета с id " + item.getId());
        itemRepository.updateItem(item);
    }

    @Override
    public Item getItem(int itemId) {
        log.info("Получение предмета с id " + itemId);
        return itemRepository.getItem(itemId);
    }

    @Override
    public List<Item> getItemsByUserSearch(String text, int userId) {
        log.info("Получение списка всех предметов пользователя с фильтром " + text);
        return itemRepository.getItemsByUserSearch(text, userId);
    }

    @Override
    public List<Item> getUserItems(int userId) {
        log.info("Получение списка всех предметов пользователя с id " + userId);
        return itemRepository.getUserItems(userId);
    }

    @Override
    public boolean isItemExists(int itemId) {
        log.info("Проверка существовария предмета с id " + itemId);
        return itemRepository.isItemExists(itemId);
    }

    @Override
    public void isUserExistsOrException(int userId) {
        if (!userService.isUserExists(userId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
    }
}
