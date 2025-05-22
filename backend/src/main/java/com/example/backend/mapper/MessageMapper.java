package com.example.backend.mapper;

import com.example.backend.dto.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.model.User;
import com.example.backend.model.Match;

public class MessageMapper {

    /** Entity → DTO */
    public static MessageDTO toDto(Message msg) {
        if (msg == null) return null;
        MessageDTO dto = new MessageDTO();
        dto.setId(msg.getId());
        dto.setUserSenderId(msg.getUserSender().getId());
        dto.setUserReceiverId(msg.getUserReceiver().getId());
        dto.setMatchId(msg.getMatch().getId());
        dto.setContent(msg.getContent());
        dto.setDateTime(msg.getDateTime());
        return dto;
    }

    /** DTO → Entity */
    public static Message toEntity(MessageDTO dto) {
        if (dto == null) return null;

        Message msg = new Message();
        msg.setId(dto.getId());
        msg.setContent(dto.getContent());
        msg.setDateTime(dto.getDateTime());

        // stub just the IDs; service layer will load the full objects if needed
        User sender = new User();
        sender.setId(dto.getUserSenderId());
        msg.setUserSender(sender);

        User receiver = new User();
        receiver.setId(dto.getUserReceiverId());
        msg.setUserReceiver(receiver);

        Match match = new Match();        // use no-arg + setter
        match.setId(dto.getMatchId());
        msg.setMatch(match);

        return msg;
    }
}

