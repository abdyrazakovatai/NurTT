package dev.nurtt.controller;

import dev.nurtt.dto.request.ClubRequest;
import dev.nurtt.model.Club;
import dev.nurtt.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/club")
@RequiredArgsConstructor
public class ClubApi {
    private final ClubService clubService;

    @PostMapping("/createClub/")
    public Club creatClub(@RequestBody ClubRequest clubRequest){
        return clubService.createClub(clubRequest);
    }
}
