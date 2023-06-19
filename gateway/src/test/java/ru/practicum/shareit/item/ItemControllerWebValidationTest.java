package ru.practicum.shareit.item;

import static org.hamcrest.Matchers.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.item.controller.ItemClient;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerWebValidationTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemClient itemClient;

    @Test
    void addItemBlankNameShouldReturnBadRequest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "", "Описание дрели", true,
            null, null, null, null);

        mvc.perform(post("/items")
            .header("X-Sharer-User-Id", 1)
            .content(mapper.writeValueAsString(itemDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400, 404))));
    }

    @Test
    void addItemBlankDescriptionShouldReturnBadRequest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "Дрель", "", true,
            null, null, null, null);

        mvc.perform(post("/items")
            .header("X-Sharer-User-Id", 4)
            .content(mapper.writeValueAsString(itemDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400, 404))));
    }
}
