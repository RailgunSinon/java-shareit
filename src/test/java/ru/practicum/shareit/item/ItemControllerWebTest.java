package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.CommentMapper;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.controller.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.service.ItemService;

@WebMvcTest(controllers = ItemController.class)
@Import({ItemMapper.class, BookingMapper.class, CommentMapper.class})
public class ItemControllerWebTest {

    @MockBean
    ItemService itemService;
    @MockBean
    BookingService bookingService;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    CommentMapper commentMapper;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void contextTest(){

    }
}
