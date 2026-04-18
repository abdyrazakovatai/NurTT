package dev.nurtt.dto.response;

import dev.nurtt.model.enums.TournamentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TournamentResponse {
    Long tournamentId;
    String tournamentName;
    int season;
    String clubName;
    TournamentType tournamentType;
}
