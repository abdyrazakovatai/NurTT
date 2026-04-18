package dev.nurtt.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class RatingHistoryResponse {
    private Long    matchId;
    private int     oldRating;
    private int     newRating;
    private int     delta;
    private Instant createdAt;
}

