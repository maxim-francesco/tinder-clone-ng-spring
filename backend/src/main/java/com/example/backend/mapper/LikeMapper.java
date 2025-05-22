package com.example.backend.mapper;

import com.example.backend.dto.LikeDTO;
import com.example.backend.model.Like;
import com.example.backend.model.User;

public class LikeMapper {

    /** Entity → DTO */
    public static LikeDTO toDto(Like like) {
        if (like == null) return null;
        LikeDTO dto = new LikeDTO();
        dto.setId(like.getId());
        dto.setUserFromId(like.getUserFrom().getId());
        dto.setUserToId(like.getUserTo().getId());
        dto.setLiked(like.getLiked());
        dto.setCreatedAt(like.getCreatedAt());
        return dto;
    }

    /** DTO → Entity */
    public static Like toEntity(LikeDTO dto) {
        if (dto == null) return null;
        Like like = new Like();
        like.setId(dto.getId());
        // only set reference by id; the service layer should fetch the full User entity
        like.setUserFrom(new User(dto.getUserFromId(), null, null, null));
        like.setUserTo(new User(dto.getUserToId(), null, null, null));
        like.setLiked(dto.getLiked());
        like.setCreatedAt(dto.getCreatedAt());
        return like;
    }
}

