package com.example.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Like, carrying just the primitive fields
 * so that your REST API never exposes Hibernate proxies.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Long id;
    private Long userFromId;
    private Long userToId;
    private Boolean liked;
    private LocalDateTime createdAt;
}

