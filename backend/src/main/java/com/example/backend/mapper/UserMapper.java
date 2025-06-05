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

        Profile p = new Profile.Builder()
                .name(dto.getProfileName())
                .age(dto.getProfileAge())
                .gender(dto.getProfileGender())
                .bio(dto.getProfileBio())
                .location(dto.getProfileLocation())
                .build();

        u.setProfile(p);
        return u;
    }

    public static UserDTO toDto(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        // If you don’t want to expose the hash, you can set password to null here:
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
