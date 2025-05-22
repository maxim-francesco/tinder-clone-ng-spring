package com.example.backend;

import com.example.backend.controller.LikeController;
import com.example.backend.dto.LikeDTO;
import com.example.backend.mapper.LikeMapper;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Like makeEntity(Long id, Long fromId, Long toId, Boolean liked, LocalDateTime ts) {
        Like l = new Like();
        l.setId(id);
        // only IDs matter for mapping
        l.setUserFrom(new com.example.backend.model.User(){ { setId(fromId); } });
        l.setUserTo(new com.example.backend.model.User(){ { setId(toId); } });
        l.setLiked(liked);
        l.setCreatedAt(ts);
        return l;
    }

    private LikeDTO makeDto(Long id, Long fromId, Long toId, Boolean liked, LocalDateTime ts) {
        LikeDTO dto = new LikeDTO();
        dto.setId(id);
        dto.setUserFromId(fromId);
        dto.setUserToId(toId);
        dto.setLiked(liked);
        dto.setCreatedAt(ts);
        return dto;
    }

    @Test
    void testAddLike_Success() throws Exception {
        // input DTO (no id, no createdAt)
        LikeDTO in = makeDto(null, 1L, 2L, true, null);

        // service returns full entity
        LocalDateTime now = LocalDateTime.of(2025,5,22,15,30,0);
        Like saved = makeEntity(42L, 1L, 2L, true, now);
        Mockito.when(likeService.addLike(any(Like.class))).thenReturn(saved);

        // perform request
        mockMvc.perform(post("/like/addlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.userFromId").value(1))
                .andExpect(jsonPath("$.userToId").value(2))
                .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.createdAt").value("2025-05-22T15:30:00"));
    }

    @Test
    void testAddLike_ServiceThrowsException() throws Exception {
        LikeDTO in = makeDto(null, 1L, 2L, true, null);
        Mockito.when(likeService.addLike(any(Like.class)))
                .thenThrow(new RuntimeException("oops"));

        mockMvc.perform(post("/like/addlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testAddLike_InvalidJson() throws Exception {
        String badJson = "{ not valid }";

        mockMvc.perform(post("/like/addlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }
}
