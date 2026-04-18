package dev.nurtt.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class LeagueResponse {
    Long leagueId;
    String name;
}
