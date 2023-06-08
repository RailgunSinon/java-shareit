package ru.practicum.shareit.request.service;

import java.util.List;
import ru.practicum.shareit.request.model.ItemRequest;

public interface RequestService {

    void addItemRequest(ItemRequest itemRequest);

    ItemRequest getItemRequestById(long requestId,long userId);

    List<ItemRequest> getUserRequestsById(long userId);

    List<ItemRequest> getOtherUsersRequests(long userId,int from,int size);
}
