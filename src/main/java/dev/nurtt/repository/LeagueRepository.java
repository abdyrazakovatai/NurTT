package dev.nurtt.repository;

import dev.nurtt.exception.NotFoundException;
import dev.nurtt.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    boolean existsByTournamentIdAndLevel(Long tournamentId, int level);

    Optional<League> findById(Long id);

    List<League> findByTournamentId(Long id);

    default League getLeagueById(Long id){
        return findById(id).orElseThrow(
                () -> new NotFoundException("League with id: " + id + "not found")
        );
    }
}
