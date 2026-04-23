package dev.nurtt.controller;

import dev.nurtt.dto.request.ClubRequest;
import dev.nurtt.model.Club;
import dev.nurtt.service.ClubService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/club")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Club", description = "Управление клубами")
public class ClubApi {

    private final ClubService clubService;

    @Operation(
            summary = "Создать клуб",
            description = "Создаёт новый теннисный клуб"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клуб создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные")
    })
    @PostMapping("/createClub")  // убрал лишний слэш в конце
    public Club createClub(@RequestBody ClubRequest clubRequest) {
        return clubService.createClub(clubRequest);
    }
}