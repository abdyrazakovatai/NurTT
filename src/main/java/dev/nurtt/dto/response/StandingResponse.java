package dev.nurtt.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StandingResponse {
    private int    place;        // место в таблице (1..6)
    private Long   slotId;
    private Long   playerId;
    private String playerName;
    private int    wins;         // победы
    private int    losses;       // поражения
    private int    setsWon;      // выигранные партии
    private int    setsLost;     // проигранные партии
    private int    setsDiff;     // разница партий (setsWon - setsLost)
    private int    matchesPlayed;
}
