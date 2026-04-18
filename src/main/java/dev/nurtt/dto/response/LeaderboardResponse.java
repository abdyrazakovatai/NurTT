package dev.nurtt.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeaderboardResponse {
    private Long   playerId;
    private String fullName;
    private int    globalRating;
}
