
package com.example.backend.controller;
import com.example.backend.dto.LikeDTO;
import com.example.backend.mapper.LikeMapper;
import com.example.backend.model.Like;
import com.example.backend.service.LikeService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /** POST /like/addlike → 201 Created + LikeDTO */
    @PostMapping("/addlike")
    public ResponseEntity<LikeDTO> addLike(@RequestBody LikeDTO dto) {
        try {
            // 1) Map DTO → entity
            Like entity = LikeMapper.toEntity(dto);

            // 2) Delegate to service
            Like saved = likeService.addLike(entity);

            // 3) Map entity → DTO
            LikeDTO out = LikeMapper.toDto(saved);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(out);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
