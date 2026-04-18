package dev.nurtt.controller;

import dev.nurtt.dto.request.LeagueRequest;
import dev.nurtt.dto.response.LeagueResponse;
import dev.nurtt.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/league")
@RequiredArgsConstructor
public class LeagueApi {
    private final LeagueService leagueService;

    @PostMapping("/createLeague/")
    public LeagueResponse createLeague(@RequestBody LeagueRequest leagueRequest){
        return leagueService.createLeague(leagueRequest);
    }
}
