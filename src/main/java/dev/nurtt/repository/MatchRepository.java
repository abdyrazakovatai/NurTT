package dev.nurtt.repository;

import dev.nurtt.exception.NotFoundException;
import dev.nurtt.model.Match;
import dev.nurtt.model.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByLeagueIdOrderById(Long leagueId);

    // Для standings — только completed и ranked
    List<Match> findByLeagueIdAndStatusAndRankedTrue(Long leagueId, MatchStatus status);

    default Match getMatchById(Long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException("Match with id: " + id + " not found"));
    }

}