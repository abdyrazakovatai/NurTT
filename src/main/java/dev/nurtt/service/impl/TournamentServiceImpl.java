package dev.nurtt.service.impl;

import dev.nurtt.dto.request.TournamentRequest;
import dev.nurtt.dto.response.TournamentResponse;
import dev.nurtt.exception.AlreadyExistsException;
import dev.nurtt.exception.BadRequestException;
import dev.nurtt.model.*;
import dev.nurtt.model.enums.MatchStatus;
import dev.nurtt.model.enums.TournamentStatus;
import dev.nurtt.repository.*;
import dev.nurtt.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final ClubRepository clubRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueSlotRepository leagueSlotRepository;
    private final MatchRepository matchRepository;

    private static final int LEAGUE_SIZE = 6;

    @Override
    public TournamentResponse createTournament(TournamentRequest tournamentRequest) {
        Club club = clubRepository.getClubById(tournamentRequest.getClubId());

        boolean exists = tournamentRepository.existsByClubIdAndSeason(tournamentRequest.getClubId(), tournamentRequest.getSeason());
        if (exists){
            throw new AlreadyExistsException("Tournament for season " + tournamentRequest.getSeason() + " already exists");
        }

        Tournament tournament = new Tournament();
        tournament.setClub(club);
        tournament.setName(tournamentRequest.getName());
        tournament.setTournamentType(tournamentRequest.getTournamentType());
        tournament.setSeason(tournamentRequest.getSeason());
        tournament.setStatus(TournamentStatus.REGISTRATION);
        tournamentRepository.save(tournament);

        return TournamentResponse.builder()
                .tournamentId(tournament.getId())
                .tournamentType(tournament.getTournamentType())
                .clubName(tournament.getClub().getName())
                .build();
    }

    @Override
    public TournamentResponse start(Long id) {
        Tournament tournament = tournamentRepository.getTournamentById(id);

        if (tournament.getStatus() != TournamentStatus.REGISTRATION){
            throw new BadRequestException("Tournament already started or finished");
        }
        List<League> leagues = leagueRepository.findByTournamentId(id);

        if (leagues.isEmpty()){
            throw new BadRequestException("Tournament has no leagues");
        }
        for (League league : leagues) {
            Long count = leagueSlotRepository.countByLeagueIdAndActiveTrue(league.getId());
            if (count < 2 || count > LEAGUE_SIZE) {
                throw new BadRequestException(
                        "League '" + league.getName() + "' must have 2-6 players"
                );
            }
        }

        for (League league : leagues) {
            generationRoundRobin(league);
        }

        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setStartedAt(Instant.now());

        Tournament save = tournamentRepository.save(tournament);

        return TournamentResponse
                .builder()
                .tournamentId(save.getId())
                .tournamentName(save.getName())
                .season(tournament.getSeason())
                .clubName(tournament.getClub().getName())
                .tournamentType(save.getTournamentType())
                .build();

    }

    private void generationRoundRobin(League league){
        List<LeagueSlot> slots = leagueSlotRepository
                .findByLeagueIdAndActiveTrueOrderByJoinedAt(league.getId());

        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < slots.size() - 1; i++) {
            for (int j = i + 1; j < slots.size(); j++) {
                Match match = new Match();
                match.setLeague(league);
                match.setSlot1(slots.get(i));
                match.setSlot2(slots.get(j));
                match.setStatus(MatchStatus.SCHEDULED);
                // Если хотя бы один игрок гость — матч не ранкед
                match.setRanked(
                        !slots.get(i).getPlayer().isGuest() &&
                                !slots.get(j).getPlayer().isGuest()
                );
                matches.add(match);
            }
        }

        matchRepository.saveAll(matches);
    }
}