package dev.nurtt.service.impl;

import dev.nurtt.dto.request.LeagueRequest;
import dev.nurtt.dto.response.LeagueResponse;
import dev.nurtt.exception.AlreadyExistsException;
import dev.nurtt.exception.BadRequestException;
import dev.nurtt.model.League;
import dev.nurtt.model.Tournament;
import dev.nurtt.model.enums.TournamentStatus;
import dev.nurtt.repository.LeagueRepository;
import dev.nurtt.repository.TournamentRepository;
import dev.nurtt.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {
    private final TournamentRepository tournamentRepository;
    private final LeagueRepository leagueRepository;

    @Override
    public LeagueResponse createLeague(LeagueRequest leagueRequest) {
        Tournament tournament = tournamentRepository.getTournamentById(leagueRequest.getTournamentId());

        if (tournament.getStatus() != TournamentStatus.REGISTRATION) {
            throw new BadRequestException("Cannot add league after tournament started");
        }

        boolean levelExists = leagueRepository.existsByTournamentIdAndLevel(leagueRequest.getTournamentId(), leagueRequest.getLevel());
        if (levelExists) {
            throw new AlreadyExistsException("League with level " + leagueRequest.getLevel() + " already exists");
        }

        League league = new League();
        league.setTournament(tournament);
        league.setName(leagueRequest.getName());
        league.setLevel(leagueRequest.getLevel());

        League saved = leagueRepository.save(league);

        recalculateSuperAndBottom(tournament.getId());
        return LeagueResponse.builder()
                .leagueId(saved.getId())
                .name(saved.getName())
                .build();
    }

    private void recalculateSuperAndBottom(Long tournamentId) {
        List<League> leagues = leagueRepository.findByTournamentId(tournamentId);

        int minLevel = leagues.stream().mapToInt(League::getLevel).min().getAsInt();
        int maxLevel = leagues.stream().mapToInt(League::getLevel).max().getAsInt();

        for (League l : leagues) {
            l.setSuperLeague(l.getLevel() == minLevel);   // level=1 → true
            l.setBottomLeague(l.getLevel() == maxLevel);  // level=4 → true
        }
        leagueRepository.saveAll(leagues);
    }
}
