package dev.nurtt.controller;

import dev.nurtt.dto.response.LeaderboardResponse;
import dev.nurtt.dto.response.RatingHistoryResponse;
import dev.nurtt.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Rating", description = "Рейтинг игроков")
public class RatingController {
    private final RatingService ratingService;

    @Operation(
            summary = "Топ игроков",
            description = "Все игроки отсортированные по глобальному рейтингу"
    )
    @GetMapping("/leaderboard")
    public List<LeaderboardResponse> getLeaderboard() {
        return ratingService.getLeaderboard();
    }

    @Operation(
            summary = "История рейтинга игрока",
            description = """
                    Формула расчёта:
                    Δ = [(100 − (Rв − Rп)) / 10] × k × D
                    k: Super Liga=1.5, Liga A=1.2, Liga B=1.0
                    D: 3:0=1.0, 3:1=0.9, 3:2=0.8
                    """
    )

    @GetMapping("/player/{playerId}/history")
    public List<RatingHistoryResponse> getHistory(@PathVariable Long playerId) {
        return ratingService.getHistory(playerId);
    }
}
