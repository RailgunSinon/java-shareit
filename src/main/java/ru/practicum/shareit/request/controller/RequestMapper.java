package ru.practicum.shareit.request.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.controller.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final ModelMapper modelMapper = new ModelMapper();
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemService itemService;

    public ItemRequestDto convertToDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = modelMapper.map(itemRequest,ItemRequestDto.class);
        itemRequestDto.setRequesterId(itemRequest.getId());
        List<Item> items = itemService.getItemsByRequestId(itemRequest.getId());
        itemRequestDto.setItems(itemMapper.convertToDtoListOfItems(items,false));
        return itemRequestDto;
    }

    public List<ItemRequestDto> convertToDtoListOfItemRequests(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests){
            itemRequestDtos.add(convertToDto(itemRequest));
        }
        return itemRequestDtos;
    }

    public ItemRequest convertToEntity(ItemRequestDto itemRequestDto, long userId) {
        ItemRequest itemRequest = modelMapper.map(itemRequestDto,ItemRequest.class);
        User requester = userService.getUserById(userId);
        itemRequest.setRequester(requester);
        if(itemRequest.getCreated() == null){
            itemRequest.setCreated(LocalDateTime.now());
        }
        return itemRequest;
    }

}
