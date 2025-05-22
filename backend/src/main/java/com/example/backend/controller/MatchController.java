// src/main/java/com/example/backend/controller/MatchController.java
package com.example.backend.controller;

import com.example.backend.dto.MatchDTO;
import com.example.backend.mapper.MatchMapper;
import com.example.backend.model.Match;
import com.example.backend.service.MatchService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * POST /match/add
     * Accepts MatchDTO, maps to entity, creates match, then returns DTO
     */
    @PostMapping("/add")
    public ResponseEntity<MatchDTO> addMatch(@RequestBody MatchDTO dto) {
        // DTO → entity
        Match toSave = MatchMapper.toEntity(dto);
        // create (service handles loading full User entities)
        Match saved = matchService.createMatch(toSave.getUser1().getId(), toSave.getUser2().getId());
        // entity → DTO
        MatchDTO out = MatchMapper.toDto(saved);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(out);
    }

    /**
     * GET /match/findbyid/{userId}
     * Returns all matches for user as DTOs
     */
    @GetMapping("/findbyid/{userId}")
    public ResponseEntity<List<MatchDTO>> findMatches(@PathVariable Long userId) {
        List<Match> matches = matchService.getMatchesForUser(userId);
        List<MatchDTO> dtos = matches.stream()
                .map(MatchMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * DELETE /match/deletebyid/{matchId}
     * Deletes by ID; returns 204 No Content
     */
    @DeleteMapping("/deletebyid/{matchId}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return ResponseEntity.noContent().build();
    }
}
