package com.example.backend.mapper;

import com.example.backend.dto.UserDTO;
import com.example.backend.model.Profile;
import com.example.backend.model.User;

public class UserMapper {

    public static User toEntity(UserDTO dto) {
        User u = new User();
        u.setId(dto.getId());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());

        Profile p = new Profile();
        p.setName(dto.getProfileName());
        p.setAge(dto.getProfileAge());
        p.setGender(dto.getProfileGender());
        p.setBio(dto.getProfileBio());
        p.setLocation(dto.getProfileLocation());

        u.setProfile(p);
        return u;
    }

    public static UserDTO toDto(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        // nu expune parola dacă nu vrei: dto.setPassword(null);
        dto.setPassword(u.getPassword());

        Profile p = u.getProfile();
        if (p != null) {
            dto.setProfileName(p.getName());
            dto.setProfileAge(p.getAge());
            dto.setProfileGender(p.getGender());
            dto.setProfileBio(p.getBio());
            dto.setProfileLocation(p.getLocation());
        }
        return dto;
    }
}

