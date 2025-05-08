package com.example.backend.service;

import com.example.backend.model.Profile;
import com.example.backend.model.User;
import com.example.backend.repository.ProfileRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public UserService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User update(Long id, User user) {
        Optional<User> updatedUser = userRepository.findById(id);
        if (updatedUser.isPresent()) {
            updatedUser.get().setEmail(user.getEmail());
            updatedUser.get().setPassword(user.getPassword());
            Optional<Profile> updatedProfile = profileRepository.findById(user.getProfile().getId());
            if (updatedProfile.isPresent()) {
                updatedProfile.get().setAge(user.getProfile().getAge());
                updatedProfile.get().setName(user.getProfile().getName());
                updatedProfile.get().setGender(user.getProfile().getGender());
                updatedProfile.get().setBio(user.getProfile().getBio());
                updatedProfile.get().setLocation(updatedProfile.get().getLocation());
                profileRepository.save(updatedProfile.get());
            }
            return userRepository.save(updatedUser.get());
        }
        return null;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
