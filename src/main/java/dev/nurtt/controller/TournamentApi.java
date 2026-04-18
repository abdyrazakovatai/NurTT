package dev.nurtt.controller;

import dev.nurtt.dto.request.TournamentRequest;
import dev.nurtt.dto.response.TournamentResponse;
import dev.nurtt.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournament")
@RequiredArgsConstructor
public class TournamentApi {
    private final TournamentService tournamentService;

    @PostMapping("/addTournament/")
    public TournamentResponse createTournament(@RequestBody TournamentRequest tournamentRequest) {
        return tournamentService.createTournament(tournamentRequest);
    }

    @PostMapping("/startTournament/{id}/")
    public TournamentResponse startTournament(@PathVariable Long id) {
        return tournamentService.start(id);
    }
}
