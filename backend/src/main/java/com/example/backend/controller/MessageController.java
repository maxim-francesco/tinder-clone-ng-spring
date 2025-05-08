package com.example.backend.controller;

import com.example.backend.model.Message;
import com.example.backend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message newMessage =  messageService.sendMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.OK);
    }

    @GetMapping("/allbymatch/{matchId}")
    public ResponseEntity<List<Message>> getAllByMatchId(@PathVariable Long matchId) {
        List<Message> messages = messageService.getAllMessagesByMatch(matchId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
