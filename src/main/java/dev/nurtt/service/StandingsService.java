package dev.nurtt.service;

import dev.nurtt.dto.response.StandingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StandingsService {
    List<StandingResponse> getStandings(Long leagueId);

}
