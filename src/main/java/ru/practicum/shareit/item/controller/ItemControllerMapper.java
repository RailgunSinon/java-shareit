package ru.practicum.shareit.item.controller;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

@Component
public class ItemControllerMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public ItemDto convertToDto(Item item) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        return itemDto;
    }

    public List<ItemDto> convertToDtoListOfItems(List<Item> items) {
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(convertToDto(item));
        }
        return itemDtos;
    }

    public Item convertToEntity(ItemService itemService, ItemDto itemDto, int itemId, int userId) {
        Item item = modelMapper.map(itemDto, Item.class);
        if (itemId != 0) {
            item.setId(itemId);
        }
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
}
