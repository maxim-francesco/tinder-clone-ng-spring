// src/main/java/com/example/backend/controller/UserController.java
package com.example.backend.controller;

import com.example.backend.dto.UserDTO;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /user/all
     * → 200 OK + JSON array of UserDTO (password = null inside each DTO)
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();

        // Map each User → UserDTO, then null out the password in the DTO
        List<UserDTO> dtos = users.stream()
                .map(UserMapper::toDto)     // convert entity → DTO
                .peek(dto -> dto.setPassword(null)) // ensure password is not exposed
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * POST /user/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody User payload) {
        if (payload.getEmail() == null || payload.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        User saved = userService.saveUser(payload);
        UserDTO out = UserMapper.toDto(saved);
        out.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    /**
     * POST /user/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and password must be provided");
        }

        User user = userService.findByEmail(request.getEmail());
        if (user == null || !userService.checkPassword(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }

        UserDTO out = UserMapper.toDto(user);
        out.setPassword(null);
        return ResponseEntity.ok(out);
    }

    /** DTO used only for capturing login‐request fields */
    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
