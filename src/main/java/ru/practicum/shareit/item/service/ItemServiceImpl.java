package ru.practicum.shareit.item.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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
}
