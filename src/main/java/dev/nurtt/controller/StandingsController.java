package dev.nurtt.controller;

import dev.nurtt.dto.response.StandingResponse;
import dev.nurtt.service.StandingsService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Standings", description = "Таблица мест в лиге")
public class StandingsController {
    private final StandingsService standingsService;

    @Operation(
            summary = "Таблица мест в лиге",
            description = """
        Сортировка:
        1. Победы (больше — выше)
        2. Разница партий setsWon - setsLost (больше — выше)
        3. Личный матч при равенстве
        Гостевые игроки не показываются
        """
    )
    @GetMapping("/league/{leagueId}")
    public List<StandingResponse> getStandings(@PathVariable Long leagueId) {
        return standingsService.getStandings(leagueId);
    }
}
