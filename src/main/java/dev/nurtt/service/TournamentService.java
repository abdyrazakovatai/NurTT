package dev.nurtt.service;

import dev.nurtt.dto.request.TournamentRequest;
import dev.nurtt.dto.response.TournamentResponse;
import org.springframework.stereotype.Service;

@Service
public interface TournamentService {
    TournamentResponse createTournament(TournamentRequest tournamentRequest);

    TournamentResponse start(Long id);
}
