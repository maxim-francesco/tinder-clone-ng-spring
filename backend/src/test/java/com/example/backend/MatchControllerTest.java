package com.example.backend;

import com.example.backend.controller.MatchController;
import com.example.backend.model.Match;
import com.example.backend.model.User;
import com.example.backend.service.MatchService;
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

@WebMvcTest(MatchController.class)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test pentru POST /match/add
    @Test
    public void testAddMatch_Success() throws Exception {
        // Creăm doi utilizatori pentru match
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setPassword("pass1");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");

        // Obiectul de intrare: match fără id, dar cu user1 și user2
        Match inputMatch = new Match();
        inputMatch.setUser1(user1);
        inputMatch.setUser2(user2);

        // Obiectul așteptat după crearea match-ului (cu id generat)
        Match savedMatch = new Match();
        savedMatch.setId(100L);
        savedMatch.setUser1(user1);
        savedMatch.setUser2(user2);

        // Configurăm mock-ul serviciului: la apelul createMatch se returnează savedMatch
        Mockito.when(matchService.createMatch(any(User.class), any(User.class))).thenReturn(savedMatch);

        mockMvc.perform(post("/match/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputMatch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.user1.id").value(1))
                .andExpect(jsonPath("$.user2.id").value(2));
    }

    // Test pentru GET /match/findbyid/{userId}
    @Test
    public void testFindMatches_Success() throws Exception {
        // Creăm doi match-uri dummy pentru un utilizator
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setPassword("pass1");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");

        Match match1 = new Match();
        match1.setId(101L);
        match1.setUser1(user1);
        match1.setUser2(user2);

        Match match2 = new Match();
        match2.setId(102L);
        match2.setUser1(user1);
        match2.setUser2(user2);

        List<Match> matches = Arrays.asList(match1, match2);

        // Configurăm mock-ul: pentru userId = 1 se returnează lista de match-uri
        Mockito.when(matchService.getMatchesForUser(eq(1L))).thenReturn(matches);

        mockMvc.perform(get("/match/findbyid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[1].id").value(102L));
    }

    // Test pentru DELETE /match/deletebyid/{matchId}
    @Test
    public void testDeleteMatch_Success() throws Exception {
        // Configurăm mock-ul pentru metoda de ștergere (metodă void)
        Mockito.doNothing().when(matchService).deleteMatch(eq(100L));

        mockMvc.perform(delete("/match/deletebyid/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
