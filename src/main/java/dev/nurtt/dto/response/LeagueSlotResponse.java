package dev.nurtt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LeagueSlotResponse {
    private Long leagueSlotId;
    private Long leagueId;
    private Long playerId;
    private String message;
}
