package dev.nurtt.controller;

import dev.nurtt.dto.request.RegisterPlayerRequest;
import dev.nurtt.dto.response.LeagueSlotPlayerResponse;
import dev.nurtt.dto.response.LeagueSlotResponse;
import dev.nurtt.service.LeagueSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/league-slots")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "League Slot", description = "Управление игроками в лигах")
public class LeagueSlotController {
    private final LeagueSlotService leagueSlotService;

    @Operation(
            summary = "Добавить игрока в лигу",
            description = "Регистрирует игрока в лиге. Максимум 6 игроков в лиге"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Игрок добавлен"),
            @ApiResponse(responseCode = "400", description = "Лига заполнена (6/6)"),
            @ApiResponse(responseCode = "409", description = "Игрок уже в этой лиге")
    })
    @PostMapping("/registerPlayer")
    public LeagueSlotResponse registerPlayer(@RequestBody RegisterPlayerRequest request) {
        return leagueSlotService.registerPlayer(request);
    }

    @Operation(
            summary = "Получить slotId по playerId",
            description = "Нужно для получения winnerSlotId при вводе результата матча"
    )
    @GetMapping("/player/{playerId}")
    public LeagueSlotPlayerResponse getSlotByPlayer(@PathVariable Long playerId) {
        return leagueSlotService.getSlotPlayer(playerId);
    }
}
