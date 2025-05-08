package com.example.backend.service;

import com.example.backend.model.Like;
import com.example.backend.model.Match;
import com.example.backend.repository.LikeRepository;
import com.example.backend.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, MatchRepository matchRepository) {
        this.likeRepository = likeRepository;
        this.matchRepository = matchRepository;
    }

    public Like addLike(Like like) {
        Like newLike = likeRepository.save(like);
        Optional<Like> reciprocalLike = likeRepository.findByUserFromAndUserTo(like.getUserFrom(), like.getUserTo());
        if (reciprocalLike.isPresent() && Boolean.TRUE.equals(reciprocalLike.get().getLiked()) && Boolean.TRUE.equals(like.getLiked())) {
            Match newMatch = new Match(like.getUserFrom(), like.getUserTo());
            matchRepository.save(newMatch);
        }
        return newLike;
    }
}
