package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.controller.UserClient;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

@WebMvcTest(controllers = UserController.class)
public class UserControllerWebValidationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserClient userClient;

    @Test
    void addUserBadEmailShouldReturnBadRequest() throws Exception {
        UserDto userDto = new UserDto(1, "testUserOne", "testUserOne");

        mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

    @Test
    void addUserBlankEmailShouldReturnBadRequest() throws Exception {
        UserDto userDto = new UserDto(1, "testUserOne", "");

        mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }

    @Test
    void addUserBlankNameShouldReturnBadRequest() throws Exception {
        UserDto userDto = new UserDto(1, "", "testUserOne@yandex.ru");

        mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(in(List.of(400))));
    }
}
