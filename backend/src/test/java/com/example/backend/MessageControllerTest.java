package com.example.backend;

import com.example.backend.controller.MessageController;
import com.example.backend.dto.MessageDTO;
import com.example.backend.mapper.MessageMapper;
import com.example.backend.model.Match;
import com.example.backend.model.Message;
import com.example.backend.model.User;
import com.example.backend.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private Message makeEntity(Long id, Long senderId, Long receiverId, Long matchId, String content, LocalDateTime dt) {
        User sender = new User(); sender.setId(senderId);
        User receiver = new User(); receiver.setId(receiverId);
        Match match = new Match(); match.setId(matchId);
        Message m = new Message();
        m.setId(id);
        m.setUserSender(sender);
        m.setUserReceiver(receiver);
        m.setMatch(match);
        m.setContent(content);
        m.setDateTime(dt);
        return m;
    }

    @Test
    void testSendMessage_Success() throws Exception {
        // input DTO (no id, no dateTime)
        MessageDTO inDto = new MessageDTO(null, 1L, 2L, 10L, "Hello!", null);

        // stub service to return full entity
        LocalDateTime now = LocalDateTime.of(2025, 5, 22, 14, 0);
        Message saved = makeEntity(100L, 1L, 2L, 10L, "Hello!", now);
        Mockito.when(messageService.sendMessage(any(Message.class))).thenReturn(saved);

        // perform POST and verify DTO in response
        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.userSenderId").value(1))
                .andExpect(jsonPath("$.userReceiverId").value(2))
                .andExpect(jsonPath("$.matchId").value(10))
                .andExpect(jsonPath("$.content").value("Hello!"))
                .andExpect(jsonPath("$.dateTime").value("2025-05-22T14:00:00"));
    }

    @Test
    void testGetAllByMatchId_Success() throws Exception {
        // two dummy entities
        Message m1 = makeEntity(1L, 1L, 2L, 10L, "First", LocalDateTime.of(2025,5,22,9,0));
        Message m2 = makeEntity(2L, 2L, 1L, 10L, "Second", LocalDateTime.of(2025,5,22,9,5));
        List<Message> list = Arrays.asList(m1, m2);

        Mockito.when(messageService.getAllMessagesByMatch(eq(10L))).thenReturn(list);

        mockMvc.perform(get("/messages/allbymatch/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("First"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content").value("Second"));
    }

    @Test
    void testSendMessage_InvalidJson() throws Exception {
        String badJson = "{ not valid }";

        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }
}
