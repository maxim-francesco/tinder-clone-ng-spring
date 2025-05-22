package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Flattened DTO for Message so the REST API only deals with IDs + simple types.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private Long userSenderId;
    private Long userReceiverId;
    private Long matchId;
    private String content;
    private LocalDateTime dateTime;
}

