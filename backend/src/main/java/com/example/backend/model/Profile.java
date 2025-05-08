package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String gender;
    private String bio;
    private String location;
}
