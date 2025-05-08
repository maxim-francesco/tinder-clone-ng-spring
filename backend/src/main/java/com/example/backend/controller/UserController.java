package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/findbyid/{userId}")
    public ResponseEntity<User> findById(@PathVariable Long userId) {
        Optional<User> user = userService.findUserById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findbyemail/{userId}")
    public ResponseEntity<User> findByEmail(@PathVariable String userId) {
        Optional<User> user = userService.findUserByEmail(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {
        User user1 = userService.saveUser(user);
        return ResponseEntity.ok(user1);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> update(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.update(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> delete(@RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }
}
