package dev.nurtt.controller;

import dev.nurtt.dto.request.TournamentRequest;
import dev.nurtt.dto.response.TournamentResponse;
import dev.nurtt.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Tournament", description = "Управление турнирами")
public class TournamentController {
    private final TournamentService tournamentService;

    @Operation(
            summary = "Создать турнир",
            description = "Создаёт новый турнир для клуба. Статус: REGISTRATION"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Турнир создан"),
            @ApiResponse(responseCode = "400", description = "Такой сезон уже существует")
    })
    @PostMapping("/create")
    public TournamentResponse createTournament(@RequestBody TournamentRequest request) {
        return tournamentService.createTournament(request);
    }

    @Operation(
            summary = "Старт турнира",
            description = "Генерирует матчи для всех лиг. Нужно минимум 2 игрока в каждой лиге"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Турнир начался, матчи сгенерированы"),
            @ApiResponse(responseCode = "400", description = "Нет лиг или недостаточно игроков")
    })
    @PostMapping("/{id}/start")
    public TournamentResponse start(@PathVariable Long id) {
        return tournamentService.start(id);
    }
}
