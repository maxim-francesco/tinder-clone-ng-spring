package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
// tell Jackson to drop any Hibernate proxy properties it doesn’t know about
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
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
