package com.example.backend.repository;

import com.example.backend.model.Like;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserFromAndUserTo(User userFrom, User userTo);
}
