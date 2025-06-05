// src/main/java/com/example/backend/service/UserService.java
package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // bean injectat

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Hash‐uiește parola și salvează user‐ul */
    public User saveUser(User user) {
        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);
        return userRepository.save(user);
    }

    /** Găsește user‐ul după email (sau null dacă nu există) */
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    /** Compară parola în clar cu hash‐ul stocat */
    public boolean checkPassword(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }

    // src/main/java/com/example/backend/service/UserService.java
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
