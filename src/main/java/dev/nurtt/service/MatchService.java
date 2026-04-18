package dev.nurtt.service;

import dev.nurtt.dto.request.MatchResultRequest;
import dev.nurtt.dto.response.MatchResponse;

import java.util.List;

public interface MatchService {
    MatchResponse submitResult(Long matchId, MatchResultRequest request);

    MatchResponse getMatch(Long matchId);

    List<MatchResponse> getMatchesByLeague(Long leagueId);
}
