package dev.nurtt.service;

import dev.nurtt.dto.request.LeagueRequest;
import dev.nurtt.dto.response.LeagueResponse;
import org.springframework.stereotype.Service;

@Service
public interface LeagueService {
    LeagueResponse createLeague(LeagueRequest leagueRequest);
}
