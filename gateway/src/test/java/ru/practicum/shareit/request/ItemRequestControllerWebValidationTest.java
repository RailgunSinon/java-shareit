package ru.practicum.shareit.request;


import static org.hamcrest.Matchers.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.request.controller.ItemRequestClient;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerWebValidationTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestClient itemRequestClient;

    LocalDateTime created = LocalDateTime.of(2023, 5, 19,
        10, 0, 0);

    @Test
    void addItemRequestBlackDescriptionShouldReturnBadRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "", 2L,
            created, null);

        mvc.perform(post("/requests")
            .header("X-Sharer-User-Id", 1)
            .content(mapper.writeValueAsString(itemRequestDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

    @Test
    void addItemRequestBadIdShouldReturnBadRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(-1, "Need Healing",
            2L, created, null);

        mvc.perform(post("/requests")
            .header("X-Sharer-User-Id", 1)
            .content(mapper.writeValueAsString(itemRequestDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

    @Test
    void addItemRequestRequesterShouldReturnBadRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Need Healing",
            -2L, created, null);

        mvc.perform(post("/requests")
            .header("X-Sharer-User-Id", 1)
            .content(mapper.writeValueAsString(itemRequestDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }
}
