package dev.nurtt.dto.response;

import dev.nurtt.model.enums.MatchStatus;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class MatchResponse {
    private Long matchId;
    private Long slot1Id;      // ← id слота (передаёшь как winnerId)
    private Long slot2Id;      // ← id слота (передаёшь как winnerId)
    private String player1Name;
    private String player2Name;
    private String winnerName;
    private int slot1Sets;     // ← количество сетов
    private int slot2Sets;     // ← количество сетов
    private MatchStatus status;
    private boolean isRanked;
    private List<SetResponse> sets;
}