package dev.nurtt.controller;

import dev.nurtt.dto.response.StandingResponse;
import dev.nurtt.service.StandingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class StandingsApi {

    private final StandingsService standingsService;

    // Таблица мест в лиге
    @GetMapping("/league/{leagueId}")
    public List<StandingResponse> getStandings(@PathVariable Long leagueId) {
        return standingsService.getStandings(leagueId);
    }
}
