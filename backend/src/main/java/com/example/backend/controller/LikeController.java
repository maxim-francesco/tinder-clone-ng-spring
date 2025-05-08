package com.example.backend.controller;

import com.example.backend.model.Like;
import com.example.backend.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/addlike")
    public ResponseEntity<Like> addlike(@RequestBody Like like) {
        try {
            Like newLike = likeService.addLike(like);
            return new ResponseEntity<>(newLike, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
