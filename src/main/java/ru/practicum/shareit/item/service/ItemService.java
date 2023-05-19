package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    void addItem(Item item);

    void updateItem(Item item);

    Item getItem(int itemId);

    List<Item> getItemsByUserSearch(String text, int userId);

    List<Item> getUserItems(int userId);
}
