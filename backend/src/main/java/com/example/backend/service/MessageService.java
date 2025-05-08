package com.example.backend.service;

import com.example.backend.model.Message;
import com.example.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message message) {
        return this.messageRepository.save(message);
    }

    public List<Message> getAllMessagesByMatch(Long id){
        return messageRepository.findByIdOrderByDateTimeAsc(id);
    }
}
