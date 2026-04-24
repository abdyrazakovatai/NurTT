package dev.nurtt.service;

import dev.nurtt.dto.response.LeaderboardResponse;
import dev.nurtt.dto.response.RatingHistoryResponse;
import dev.nurtt.model.Match;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    void recalculate(Match match);

    List<LeaderboardResponse> getLeaderboard();

    List<RatingHistoryResponse> getHistory(Long playerId);
}
