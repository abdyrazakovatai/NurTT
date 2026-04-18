package dev.nurtt.service;

import dev.nurtt.dto.request.PlayerRequest;
import dev.nurtt.dto.response.PlayerResponse;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

    PlayerResponse createPlayer(PlayerRequest playerRequest);
}
