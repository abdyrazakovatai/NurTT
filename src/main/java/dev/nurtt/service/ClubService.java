package dev.nurtt.service;

import dev.nurtt.dto.request.ClubRequest;
import dev.nurtt.model.Club;
import org.springframework.stereotype.Service;

@Service
public interface ClubService {
    Club createClub(ClubRequest clubRequest);
}
