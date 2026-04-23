package dev.nurtt.service.impl;

import dev.nurtt.dto.request.RegisterPlayerRequest;
import dev.nurtt.dto.response.LeagueSlotPlayerResponse;
import dev.nurtt.dto.response.LeagueSlotResponse;
import dev.nurtt.exception.AlreadyExistsException;
import dev.nurtt.exception.BadRequestException;
import dev.nurtt.model.League;
import dev.nurtt.model.LeagueSlot;
import dev.nurtt.model.Player;
import dev.nurtt.model.enums.TournamentStatus;
import dev.nurtt.repository.LeagueRepository;
import dev.nurtt.repository.LeagueSlotRepository;
import dev.nurtt.repository.PlayerRepository;
import dev.nurtt.service.LeagueSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueSlotServiceImpl implements LeagueSlotService {

    private final PlayerRepository playerRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueSlotRepository leagueSlotRepository;

    private static final int LEAGUE_SIZE = 6;

    @Override
    public LeagueSlotResponse registerPlayer(RegisterPlayerRequest playerRequest) {
        League league = leagueRepository.getLeagueById(playerRequest.getLeagueId());

        if (league.getTournament().getStatus() != TournamentStatus.REGISTRATION) {
            throw new AlreadyExistsException("Registration is closed");
        }

        Long activeSlots = leagueSlotRepository.countByLeagueIdAndActiveTrue(playerRequest.getLeagueId());
        if (activeSlots >= LEAGUE_SIZE) {
            throw new BadRequestException("League is full. Max " + LEAGUE_SIZE + " players");
        }

        boolean alreadyIn = leagueSlotRepository
                .existsByLeagueIdAndPlayerIdAndActiveTrue(playerRequest.getLeagueId(), playerRequest.getPlayerId());
        if (alreadyIn) {
            throw new AlreadyExistsException("Player already registered in this league");
        }

        Player player = playerRepository.getPlayerById(playerRequest.getPlayerId());

        LeagueSlot slot = new LeagueSlot();
        slot.setLeague(league);
        slot.setPlayer(player);

        LeagueSlot save = leagueSlotRepository.save(slot);

        return new LeagueSlotResponse(
                save.getId(),
                league.getId(),
                player.getId(),
                "Saved successfully"
        );
    }

    @Override
    public LeagueSlotPlayerResponse getSlotPlayer(Long id) {
        LeagueSlot slot = leagueSlotRepository.getLeagueSlotByPlayerId(id);

        return new LeagueSlotPlayerResponse(
                slot.getPlayer().getId(),
                slot.getId());
    }
}
