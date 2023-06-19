package ru.practicum.shareit.item.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
        RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> createItem(ItemDto itemDto, Long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> patchItem(ItemDto itemDto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public void deleteItem(Long itemId) {
        delete("/" + itemId);
    }

    public ResponseEntity<Object> getItem(Long id, Long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getAllUserItems(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
            "from", from,
            "size", size
        );

        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemsByUserSearch(Long userId, String search, Integer from,
        Integer size) {
        Map<String, Object> parameters = Map.of(
            "text", search,
            "from", from,
            "size", size
        );

        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> createItemComment(CommentDto commentShortDto, Long itemId,
        Long userId) {
        return post("/" + itemId + "/comment", userId, commentShortDto);
    }

}
