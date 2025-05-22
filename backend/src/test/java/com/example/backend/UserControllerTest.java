package com.example.backend;

import com.example.backend.controller.UserController;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.Profile;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // helper to build a User entity with Profile
    private User makeUser(Long id, String email) {
        Profile p = new Profile();
        p.setId(100L);
        p.setName("Alice");
        p.setAge(30);
        p.setGender("female");
        p.setBio("Hello");
        p.setLocation("City");

        User u = new User();
        u.setId(id);
        u.setEmail(email);
        u.setPassword("secret");
        u.setProfile(p);
        return u;
    }

    @Test
    void testGetAllUsers() throws Exception {
        User u1 = makeUser(1L, "a@x.com");
        User u2 = makeUser(2L, "b@y.com");
        List<User> all = Arrays.asList(u1, u2);

        Mockito.when(userService.findAll()).thenReturn(all);

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("a@x.com"))
                .andExpect(jsonPath("$[0].profileName").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].profileLocation").value("City"));
    }

    @Test
    void testFindByIdFound() throws Exception {
        User u = makeUser(5L, "find@u.com");
        Mockito.when(userService.findUserById(5L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/user/findbyid/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.profileAge").value(30));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        Mockito.when(userService.findUserById(9L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/findbyid/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByEmailFound() throws Exception {
        User u = makeUser(7L, "e@mail.com");
        Mockito.when(userService.findUserByEmail("e@mail.com"))
                .thenReturn(Optional.of(u));

        mockMvc.perform(get("/user/findbyemail/e@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.profileGender").value("female"));
    }

    @Test
    void testFindByEmailNotFound() throws Exception {
        Mockito.when(userService.findUserByEmail("no@one.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/user/findbyemail/no@one.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddUserSuccess() throws Exception {
        // Prepare DTO payload
        UserDTO in = new UserDTO(
                null,
                "new@user.com",
                "pw",
                "Bob",
                25,
                "male",
                "Hi",
                "Here"
        );

        // Stub service → returns entity with generated ID
        User saved = makeUser(3L, "new@user.com");
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("new@user.com"))
                .andExpect(jsonPath("$.profileName").value("Alice"));
    }

    @Test
    void testAddUserBadRequest() throws Exception {
        // missing email
        UserDTO in = new UserDTO(
                null, null, "pw", "X", 20, "other", "b", "l"
        );

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDTO in = new UserDTO(
                null, "upd@x.com", "npw", "Carol", 28, "female", "hey", "loc"
        );

        User existing = makeUser(4L, "old@x.com");
        Mockito.when(userService.findUserById(4L)).thenReturn(Optional.of(existing));

        User updated = makeUser(4L, "upd@x.com");
        Mockito.when(userService.update(eq(4L), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/user/update/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("upd@x.com"))
                .andExpect(jsonPath("$.profileAge").value(30));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        UserDTO in = new UserDTO();
        Mockito.when(userService.findUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/user/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isNotFound());
    }
}
