// src/main/java/com/example/backend/controller/MessageController.java
package com.example.backend.controller;

import com.example.backend.dto.MessageDTO;
import com.example.backend.mapper.MessageMapper;
import com.example.backend.model.Message;
import com.example.backend.service.MessageService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * POST /messages/send
     * Accepts MessageDTO, maps to entity, persists, then returns DTO
     */
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO dto) {
        // 1) DTO → Entity
        Message entity = MessageMapper.toEntity(dto);

        // 2) Persist
        Message saved = messageService.sendMessage(entity);

        // 3) Entity → DTO
        MessageDTO out = MessageMapper.toDto(saved);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(out);
    }

    /**
     * GET /messages/allbymatch/{matchId}
     * Retrieves all Message entities for a match and returns as DTO list
     */
    @GetMapping("/allbymatch/{matchId}")
    public ResponseEntity<List<MessageDTO>> getAllByMatchId(@PathVariable Long matchId) {
        List<Message> messages = messageService.getAllMessagesByMatch(matchId);

        List<MessageDTO> dtos = messages.stream()
                .map(MessageMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
