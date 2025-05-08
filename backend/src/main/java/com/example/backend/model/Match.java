package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user1;
    @OneToOne
    private User user2;
    private LocalDateTime matchDate;

    public Match(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
