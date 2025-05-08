package com.example.backend.controller;

import com.example.backend.model.Match;
import com.example.backend.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchController {
    private final MatchService matchService;
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/add")
    public ResponseEntity<Match> addMatch(@RequestBody Match match) {
        Match newMatch = matchService.createMatch(match.getUser1(), match.getUser2());
        return new ResponseEntity<>(newMatch, HttpStatus.CREATED);
    }

    @GetMapping("/findbyid/{userId}")
    public ResponseEntity<List<Match>> findMatches(@PathVariable Long userId) {
        List<Match> matches = matchService.getMatchesForUser(userId);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @DeleteMapping("/deletebyid/{matchId}")
    public ResponseEntity<Match> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
