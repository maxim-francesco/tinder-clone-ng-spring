package com.example.backend;

import com.example.backend.controller.MessageController;
import com.example.backend.model.Message;
import com.example.backend.model.User;
import com.example.backend.model.Match;
import com.example.backend.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test complet pentru POST /messages/send cu atribute complete pentru Message
    @Test
    public void testSendMessage_Success() throws Exception {
        // Creăm un utilizator sender
        User sender = new User();
        sender.setId(1L);
        sender.setEmail("sender@example.com");
        sender.setPassword("senderpass");

        // Creăm un utilizator receiver
        User receiver = new User();
        receiver.setId(2L);
        receiver.setEmail("receiver@example.com");
        receiver.setPassword("receiverpass");

        // Creăm un match între sender și receiver
        Match match = new Match();
        match.setId(10L);
        // Se pot seta și alte atribute ale match-ului, dacă este cazul
        match.setUser1(sender);
        match.setUser2(receiver);

        // Construim mesajul de intrare complet
        Message inputMessage = new Message();
        inputMessage.setContent("Hello, how are you?");
        inputMessage.setUserSender(sender);
        inputMessage.setUserReceiver(receiver);
        inputMessage.setMatch(match);
        // sentAt se setează automat în constructor, dar poate fi setat și manual

        // Obiectul așteptat după salvare (cu id generat)
        Message savedMessage = new Message();
        savedMessage.setId(100L);
        savedMessage.setContent("Hello, how are you?");
        savedMessage.setUserSender(sender);
        savedMessage.setUserReceiver(receiver);
        savedMessage.setMatch(match);

        // Configurăm mock-ul serviciului MessageService
        Mockito.when(messageService.sendMessage(any(Message.class))).thenReturn(savedMessage);

        // Efectuăm cererea POST și verificăm răspunsul
        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.content").value("Hello, how are you?"));
    }

    // Test pentru scenariul în care se trimite JSON invalid
    @Test
    public void testSendMessage_InvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    // Test pentru GET /messages/allbymatch/{matchId}
    @Test
    public void testGetAllByMatchId_Success() throws Exception {
        // Creăm două mesaje dummy
        Message message1 = new Message();
        message1.setId(1L);
        message1.setContent("First message");

        Message message2 = new Message();
        message2.setId(2L);
        message2.setContent("Second message");

        List<Message> messages = Arrays.asList(message1, message2);

        // Configurăm mock-ul serviciului: pentru match cu id-ul 10, returnăm lista de mesaje
        Mockito.when(messageService.getAllMessagesByMatch(eq(10L))).thenReturn(messages);

        mockMvc.perform(get("/messages/allbymatch/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("First message"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].content").value("Second message"));
    }
}
