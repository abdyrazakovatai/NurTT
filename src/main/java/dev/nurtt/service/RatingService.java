package dev.nurtt.service;

import dev.nurtt.model.Match;
import org.springframework.stereotype.Service;

@Service
public interface RatingService {
    void recalculate(Match match);
}
