package dev.nurtt.dto.request;

import lombok.*;

@Getter @Setter
@Data
public class RegisterPlayerRequest {
    private Long leagueId;
    private Long playerId;
}
