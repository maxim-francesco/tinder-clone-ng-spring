package com.example.backend.mapper;

import com.example.backend.dto.MatchDTO;
import com.example.backend.model.Match;
import com.example.backend.model.User;

public class MatchMapper {

    /** Entity → DTO */
    public static MatchDTO toDto(Match match) {
        if (match == null) return null;
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setUser1Id(match.getUser1().getId());
        dto.setUser2Id(match.getUser2().getId());
        dto.setMatchDate(match.getMatchDate());
        return dto;
    }

    /** DTO → Entity */
    public static Match toEntity(MatchDTO dto) {
        if (dto == null) return null;
        Match match = new Match();
        match.setId(dto.getId());
        match.setMatchDate(dto.getMatchDate());

        // stub doar ID-urile; service-ul va încărca entitățile complete după nevoie
        User u1 = new User();
        u1.setId(dto.getUser1Id());
        match.setUser1(u1);

        User u2 = new User();
        u2.setId(dto.getUser2Id());
        match.setUser2(u2);

        return match;
    }
}
