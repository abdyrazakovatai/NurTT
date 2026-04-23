package dev.nurtt.controller;

import dev.nurtt.dto.request.LeagueRequest;
import dev.nurtt.dto.response.LeagueResponse;
import dev.nurtt.service.LeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "League", description = "Управление лигами")
public class LeagueController {
    private final LeagueService leagueService;

    @Operation(
            summary = "Создать лигу",
            description = """
        Создаёт лигу внутри турнира.
        level=1 → Super Liga (высшая, повышение не действует)
        level=2 → Liga A
        level=3 → Liga B (низшая, понижение не действует)
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лига создана"),
            @ApiResponse(responseCode = "400", description = "Турнир уже начался"),
            @ApiResponse(responseCode = "409", description = "Лига с таким уровнем уже существует")
    })
    @PostMapping("/createLeague")
    public LeagueResponse createLeague(@RequestBody LeagueRequest request) {
        return leagueService.createLeague(request);
    }
}
