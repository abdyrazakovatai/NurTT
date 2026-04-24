package dev.nurtt.controller;

import dev.nurtt.dto.request.PlayerRequest;
import dev.nurtt.dto.response.PlayerResponse;
import dev.nurtt.service.PlayerService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Player", description = "Управление игроками")
public class PlayerController {

    private final PlayerService playerService;

    @Operation(
            summary = "Создать игрока",
            description = "Создаёт нового игрока. isGuest=true — гостевой игрок (рейтинг не считается)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Игрок создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные")
    })
    @PostMapping("/createPlayer")
    public PlayerResponse createPlayer(@RequestBody PlayerRequest playerRequest) {  // добавь @RequestBody!
        return playerService.createPlayer(playerRequest);
    }
}
