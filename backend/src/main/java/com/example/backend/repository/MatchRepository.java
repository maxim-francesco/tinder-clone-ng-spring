package com.example.backend.repository;

import com.example.backend.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUser1_IdOrUser2_Id(Long user1Id, Long user2Id);
}
