package ru.practicum.shareit.booking;

import static org.hamcrest.Matchers.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import ru.practicum.shareit.booking.controller.BookingClient;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerWebValidationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingClient bookingClient;

    private final LocalDateTime curDate = LocalDateTime.now();

    @Test
    void addBookingBadStartTimeShouldReturnBadRequest() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1,
            1, curDate.minusMinutes(30), curDate.plusHours(2));

        mvc.perform(post("/bookings").header("X-Sharer-User-Id", 2)
            .content(mapper.writeValueAsString(bookingRequestDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

    @Test
    void getCurrentBookingForUserShouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 3)
            .param("from", String.valueOf(-1)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

    @Test
    void getCurrentBookingForOwnerShouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1)
            .param("from", String.valueOf(-1)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

}
