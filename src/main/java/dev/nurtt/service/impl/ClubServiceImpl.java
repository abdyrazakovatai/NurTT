package dev.nurtt.service.impl;

import dev.nurtt.dto.request.ClubRequest;
import dev.nurtt.model.Club;
import dev.nurtt.repository.ClubRepository;
import dev.nurtt.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class  ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    @Override
    public Club createClub(ClubRequest clubRequest) {
        Club club = new Club();
        club.setName(clubRequest.getName());
        club.setCreatedAt(Instant.now());
        return clubRepository.save(club);
    }
}
