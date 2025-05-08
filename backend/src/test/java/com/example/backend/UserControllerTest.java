package com.example.backend;

import com.example.backend.controller.UserController;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test pentru GET /user/all
    @Test
    public void testGetAllUsers() throws Exception {
        // Creăm câțiva utilizatori dummy
        User user1 = new User(1L, "test1@example.com", "password1", null);
        User user2 = new User(2L, "test2@example.com", "password2", null);
        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test1@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"));
    }

    // Test pentru GET /user/findbyid/{userId} - utilizator găsit
    @Test
    public void testFindUserById_Found() throws Exception {
        User user = new User(1L, "test@example.com", "password", null);
        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/findbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    // Test pentru GET /user/findbyid/{userId} - utilizator inexistent
    @Test
    public void testFindUserById_NotFound() throws Exception {
        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/findbyid/1"))
                .andExpect(status().isNotFound());
    }

    // Test pentru GET /user/findbyemail/{userId} - găsește după email
    @Test
    public void testFindUserByEmail_Found() throws Exception {
        // Observație: endpoint-ul folosește {userId} pentru email
        User user = new User(1L, "test@example.com", "password", null);
        Mockito.when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/findbyemail/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    // Test pentru GET /user/findbyemail/{userId} - email inexistent
    @Test
    public void testFindUserByEmail_NotFound() throws Exception {
        Mockito.when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/findbyemail/test@example.com"))
                .andExpect(status().isNotFound());
    }

    // Test pentru POST /user/add
    @Test
    public void testAddUser() throws Exception {
        User inputUser = new User(null, "newuser@example.com", "password", null);
        User savedUser = new User(1L, "newuser@example.com", "password", null);

        Mockito.when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    // Test pentru PUT /user/update/{userId}
    @Test
    public void testUpdateUser() throws Exception {
        User inputUser = new User(null, "updated@example.com", "newpassword", null);
        User updatedUser = new User(1L, "updated@example.com", "newpassword", null);

        Mockito.when(userService.update(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    // Test pentru DELETE /user/delete
    @Test
    public void testDeleteUser() throws Exception {
        // Se presupune că userService.deleteUser este o metodă void
        Mockito.doNothing().when(userService).deleteUser(any(User.class));

        User userToDelete = new User(1L, "delete@example.com", "password", null);

        mockMvc.perform(delete("/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToDelete)))
                .andExpect(status().isNoContent());
    }
}

