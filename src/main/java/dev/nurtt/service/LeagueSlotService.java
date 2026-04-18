package dev.nurtt.service;

import dev.nurtt.dto.request.RegisterPlayerRequest;
import dev.nurtt.dto.response.LeagueSlotResponse;
import dev.nurtt.model.LeagueSlot;
import org.springframework.stereotype.Service;

@Service
public interface LeagueSlotService {
    LeagueSlotResponse registerPlayer(RegisterPlayerRequest playerRequest);
}
