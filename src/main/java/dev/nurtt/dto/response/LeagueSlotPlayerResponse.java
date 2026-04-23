package dev.nurtt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LeagueSlotPlayerResponse {
    private Long playerId;
    private Long slotId;
}
