package com.example.backend;

import com.example.backend.controller.LikeController;
import com.example.backend.model.Like;
import com.example.backend.service.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test de succes: se trimite un Like valid și se așteaptă un răspuns 201 CREATED
    @Test
    public void testAddLike_Success() throws Exception {
        // Obiectul de intrare; setările suplimentare se pot adăuga după nevoie
        Like inputLike = new Like();
        Like savedLike = new Like();
        savedLike.setId(1L);

        Mockito.when(likeService.addLike(any(Like.class))).thenReturn(savedLike);

        // Efectuăm cererea POST și verificăm statusul și conținutul JSON
        mockMvc.perform(post("/like/addlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputLike)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // Test pentru scenariul în care serviciul aruncă o excepție (de exemplu, eroare internă)
    @Test
    public void testAddLike_ServiceThrowsException() throws Exception {
        Like inputLike = new Like();
        // Configurăm mock-ul să arunce o excepție la apelul metodei addLike
        Mockito.when(likeService.addLike(any(Like.class)))
                .thenThrow(new RuntimeException("Service error"));

        // Efectuăm cererea POST și verificăm că se întoarce statusul 500
        mockMvc.perform(post("/like/addlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputLike)))
                .andExpect(status().isInternalServerError());
    }

    // Test pentru scenariul în care se trimite un JSON invalid (de exemplu, sintaxă greșită)
    @Test
    public void testAddLike_InvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/like/addlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}

