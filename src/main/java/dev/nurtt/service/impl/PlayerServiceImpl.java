package dev.nurtt.service.impl;

import dev.nurtt.dto.request.PlayerRequest;
import dev.nurtt.dto.response.PlayerResponse;
import dev.nurtt.model.Player;
import dev.nurtt.repository.PlayerRepository;
import dev.nurtt.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public PlayerResponse createPlayer(PlayerRequest playerRequest) {
        Player player = new Player();
        player.setFullName(playerRequest.getFullName());
        player.setEmail(playerRequest.getEmail());

        playerRepository.save(player);
        return PlayerResponse.builder()
                .id(player.getId())
                .fullName(player.getFullName())
                .email(player.getEmail())
                .build();
    }
}
