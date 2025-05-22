package com.example.backend.controller;

import com.example.backend.dto.UserDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
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

    /** GET /user/all → 200 OK + List<UserDTO> */
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> dtos = userService.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** GET /user/findbyid/{id} → 200 OK + UserDTO or 404 */
    @GetMapping("/findbyid/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    /** GET /user/findbyemail/{email} → 200 OK + UserDTO or 404 */
    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    /** POST /user/add → 201 Created + UserDTO or 400 */
    @PostMapping("/add")
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO dto) {
        if (dto.getEmail() == null || dto.getPassword() == null) {
            throw new BadRequestException("Email and password must be provided");
        }

        User toSave = UserMapper.toEntity(dto);
        User saved = userService.saveUser(toSave);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toDto(saved));
    }

    /** PUT /user/update/{id} → 200 OK + UserDTO or 404 */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id,
            @RequestBody UserDTO dto) {

        // ensure user exists or 404
        userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        // map DTO → entity and set ID
        User toUpdate = UserMapper.toEntity(dto);
        toUpdate.setId(id);

        User updated = userService.update(id, toUpdate);
        return ResponseEntity.ok(UserMapper.toDto(updated));
    }


}
