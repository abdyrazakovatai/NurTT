package dev.nurtt.controller;

import dev.nurtt.dto.response.LeaderboardResponse;
import dev.nurtt.dto.response.RatingHistoryResponse;
import dev.nurtt.repository.PlayerRepository;
import dev.nurtt.repository.RatingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class RatingApi {

    private final RatingHistoryRepository ratingHistoryRepository;
    private final PlayerRepository playerRepository;

    // История рейтинга игрока
    @GetMapping("/player/{playerId}/history")
    public List<RatingHistoryResponse> getHistory(@PathVariable Long playerId) {
        return ratingHistoryRepository
                .findByPlayerIdOrderByCreatedAtDesc(playerId)
                .stream()
                .map(h -> RatingHistoryResponse.builder()
                        .matchId(h.getMatch().getId())
                        .oldRating(h.getOldRating())
                        .newRating(h.getNewRating())
                        .delta(h.getDelta())
                        .createdAt(h.getCreatedAt())
                        .build())
                .toList();
    }

    // Топ игроков по рейтингу (лидерборд)
    @GetMapping("/leaderboard")
    public List<LeaderboardResponse> getLeaderboard() {
        return playerRepository.findAllByOrderByGlobalRatingDesc()
                .stream()
                .map(p -> LeaderboardResponse.builder()
                        .playerId(p.getId())
                        .fullName(p.getFullName())
                        .globalRating(p.getGlobalRating())
                        .build())
                .toList();
    }
}
