package dev.nurtt.controller;

import dev.nurtt.dto.request.PlayerRequest;
import dev.nurtt.dto.response.PlayerResponse;
import dev.nurtt.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class PlayerApi {

    private final PlayerService playerService;

    @PostMapping("/createPlayer")
    public PlayerResponse createPlayer(PlayerRequest playerRequest){
        return playerService.createPlayer(playerRequest);
    }
}
