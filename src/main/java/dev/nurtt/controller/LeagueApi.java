package dev.nurtt.controller;

import dev.nurtt.dto.request.LeagueRequest;
import dev.nurtt.dto.response.LeagueResponse;
import dev.nurtt.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/league")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class LeagueApi {
    private final LeagueService leagueService;

    @PostMapping("/createLeague/")
    public LeagueResponse createLeague(@RequestBody LeagueRequest leagueRequest){
        return leagueService.createLeague(leagueRequest);
    }
}
