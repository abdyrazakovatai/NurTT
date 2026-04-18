package dev.nurtt.controller;

import dev.nurtt.dto.request.MatchResultRequest;
import dev.nurtt.dto.response.MatchResponse;
import dev.nurtt.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class MatchApi {

    private final MatchService matchService;

    // Ввести результат матча
    @PutMapping("/{matchId}/result")
    public MatchResponse submitResult(@PathVariable Long matchId,
                                      @RequestBody MatchResultRequest request) {
        return matchService.submitResult(matchId, request);
    }

    // Получить матч
    @GetMapping("/{matchId}")
    public MatchResponse getMatch(@PathVariable Long matchId) {
        return matchService.getMatch(matchId);
    }

    // Все матчи лиги
    @GetMapping("/league/{leagueId}")
    public List<MatchResponse> getMatchesByLeague(@PathVariable Long leagueId) {
        return matchService.getMatchesByLeague(leagueId);
    }
}