package ru.practicum.shareit.item.repository;

import java.util.List;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {

    void addItem(Item item);

    void updateItem(Item item);

    Item getItem(int itemId);

    List<Item> getItemsByUserSearch(String text,int userId);

    List<Item> getUserItems(int userId);
}
