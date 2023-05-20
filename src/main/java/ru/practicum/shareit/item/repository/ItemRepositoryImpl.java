package ru.practicum.shareit.item.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Integer, Item> items = new HashMap<>();

    @Override
    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public Item getItem(int itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмет с таким id не был найден");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsByUserSearch(String text, int userId) {
        if (text.isBlank()){
            return new ArrayList<>();
        }
        return items.values().stream()
            .filter(user -> user.getAvailable() == true)
            .filter(user -> (user.getDescription().toLowerCase().contains(text.toLowerCase())
                || user.getName().toLowerCase().contains(text.toLowerCase())))
            .collect(Collectors.toList());
    }


    @Override
    public List<Item> getUserItems(int userId) {
        return items.values().stream()
            .filter(user -> user.getUserId() == userId)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isItemExists(int itemId) {
        if(items.containsKey(itemId)){
            return true;
        }
        return false;
    }

}
