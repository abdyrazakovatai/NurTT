package dev.nurtt.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LeagueRequest {
    private Long tournamentId;
    private String name;
    private int level;
}
