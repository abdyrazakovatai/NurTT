package dev.nurtt.controller;

import dev.nurtt.dto.request.RegisterPlayerRequest;
import dev.nurtt.dto.response.LeagueSlotResponse;
import dev.nurtt.service.LeagueSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leagueSlot")
@RequiredArgsConstructor
public class LeagueSlotApi {
    private final LeagueSlotService leagueSlotService;

    @PostMapping("/registerPlayer/")
    public LeagueSlotResponse addPlayer(@RequestBody RegisterPlayerRequest playerRequest){
        return leagueSlotService.registerPlayer(playerRequest);
    }
}
