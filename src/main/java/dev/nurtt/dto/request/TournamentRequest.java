package dev.nurtt.dto.request;

import dev.nurtt.model.enums.TournamentType;
import lombok.Getter;

@Getter
public class TournamentRequest {
    private Long clubId;
    private String name;
    private int season;
    private TournamentType tournamentType;
}
