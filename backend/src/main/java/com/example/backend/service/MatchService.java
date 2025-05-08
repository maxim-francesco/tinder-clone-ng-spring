package com.example.backend.service;

import com.example.backend.model.Match;
import com.example.backend.model.User;
import com.example.backend.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match createMatch(User user1, User user2) {
        Match match = new Match(user1, user2);
        return this.matchRepository.save(match);
    }

    public List<Match> getMatchesForUser(Long userId) {
        return this.matchRepository.findByUser1_IdOrUser2_Id(userId, userId);
    }

    public void deleteMatch(Long matchId) {
        this.matchRepository.deleteById(matchId);
    }
}
