package com.example.backend;// src/test/java/com/example/backend/controller/MatchControllerTest.java


import com.example.backend.controller.MatchController;
import com.example.backend.dto.MatchDTO;
import com.example.backend.mapper.MatchMapper;
import com.example.backend.model.Match;
import com.example.backend.model.User;
import com.example.backend.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyLong;
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

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @Autowired
    private ObjectMapper objectMapper;

    private Match makeMatch(Long id, Long u1, Long u2, LocalDateTime dt) {
        User user1 = new User(); user1.setId(u1);
        User user2 = new User(); user2.setId(u2);
        Match m = new Match();
        m.setId(id);
        m.setUser1(user1);
        m.setUser2(user2);
        m.setMatchDate(dt);
        return m;
    }

    @Test
    void testAddMatch_Success() throws Exception {
        // Prepare input DTO
        MatchDTO inputDto = new MatchDTO(null, 1L, 2L, null);

        // Stub service to return entity with ID and date
        LocalDateTime now = LocalDateTime.of(2025,5,22,10,0);
        Match saved = makeMatch(100L, 1L, 2L, now);
        Mockito.when(matchService.createMatch(eq(1L), eq(2L)))
                .thenReturn(saved);

        // Perform POST and assert DTO response
        mockMvc.perform(post("/match/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.user1Id").value(1))
                .andExpect(jsonPath("$.user2Id").value(2))
                .andExpect(jsonPath("$.matchDate").value("2025-05-22T10:00:00"));
    }

    @Test
    void testFindMatches_Success() throws Exception {
        // Prepare two matches
        Match m1 = makeMatch(101L, 1L, 2L, LocalDateTime.of(2025,5,22,9,0));
        Match m2 = makeMatch(102L, 1L, 3L, LocalDateTime.of(2025,5,22,9,30));
        List<Match> list = Arrays.asList(m1, m2);

        Mockito.when(matchService.getMatchesForUser(eq(1L)))
                .thenReturn(list);

        mockMvc.perform(get("/match/findbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].user1Id").value(1))
                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].user2Id").value(3));
    }

    @Test
    void testDeleteMatch_Success() throws Exception {
        Mockito.doNothing().when(matchService).deleteMatch(eq(100L));

        mockMvc.perform(delete("/match/deletebyid/100"))
                .andExpect(status().isNoContent());
    }
}
