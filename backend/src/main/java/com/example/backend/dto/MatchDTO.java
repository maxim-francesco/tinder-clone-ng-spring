package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Match, flattening the two User references down to IDs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Long id;
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime matchDate;
}

