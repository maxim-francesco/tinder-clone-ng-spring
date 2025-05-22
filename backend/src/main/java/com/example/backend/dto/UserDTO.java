// src/main/java/com/example/backend/dto/UserDTO.java
package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pentru User + Profile (flattened), ca apoi în mapper să reconstruieşti entităţile.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;

    // Profile fields, pe care le vei folosi în mapper
    private String profileName;
    private Integer profileAge;
    private String profileGender;
    private String profileBio;
    private String profileLocation;
}
