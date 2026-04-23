package dev.nurtt.controller;

import dev.nurtt.dto.request.MatchResultRequest;
import dev.nurtt.dto.response.MatchResponse;
import dev.nurtt.service.MatchService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Match", description = "Управление матчами")
public class MatchController {
    private final MatchService matchService;

    @Operation(
            summary = "Все матчи лиги",
            description = "Возвращает все матчи лиги. SCHEDULED — ещё не сыгран, COMPLETED — сыгран"
    )
    @GetMapping("/league/{leagueId}")
    public List<MatchResponse> getMatchesByLeague(@PathVariable Long leagueId) {
        return matchService.getMatchesByLeague(leagueId);
    }

    @Operation(
            summary = "Получить матч",
            description = "Возвращает матч с партиями"
    )
    @GetMapping("/{matchId}")
    public MatchResponse getMatch(@PathVariable Long matchId) {
        return matchService.getMatch(matchId);
    }

    @Operation(
            summary = "Ввести результат матча",
            description = """
        winnerSlotId — это slot1Id или slot2Id из ответа матча (не playerId!)
        sets — партии матча, best of 5 (до 3 побед)
        Примеры счёта: 3:0, 3:1, 3:2
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат записан"),
            @ApiResponse(responseCode = "400", description = "Матч уже сыгран или неверный счёт"),
            @ApiResponse(responseCode = "404", description = "Матч не найден")
    })
    @PutMapping("/{matchId}/result")
    public MatchResponse submitResult(
            @PathVariable Long matchId,
            @RequestBody MatchResultRequest request) {
        return matchService.submitResult(matchId, request);
    }
}