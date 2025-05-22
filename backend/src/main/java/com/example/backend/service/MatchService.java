// src/main/java/com/example/backend/service/MatchService.java
package com.example.backend.service;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Match;
import com.example.backend.model.User;
import com.example.backend.repository.MatchRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public MatchService(
            MatchRepository matchRepository,
            UserRepository userRepository
    ) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a Match between two users given their IDs.
     * Throws ResourceNotFoundException if either user is missing.
     */
    public Match createMatch(Long user1Id, Long user2Id) {
        User u1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + user1Id));
        User u2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + user2Id));

        Match match = new Match(u1, u2);
        return matchRepository.save(match);
    }

    /** Finds all matches where either participant is the given user ID */
    public List<Match> getMatchesForUser(Long userId) {
        return matchRepository.findByUser1_IdOrUser2_Id(userId, userId);
    }

    /** Deletes a match by its ID, throws 404 if missing */
    public void deleteMatch(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new ResourceNotFoundException("Match not found: " + matchId);
        }
        matchRepository.deleteById(matchId);
    }
}
