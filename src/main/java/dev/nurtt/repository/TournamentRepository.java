package dev.nurtt.repository;

import dev.nurtt.exception.NotFoundException;
import dev.nurtt.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByClubIdAndSeason(Long clubId, int season);

    Optional<Tournament> findById(Long tournamentId);

    default Tournament getTournamentById(Long tournamentId){
        return findById(tournamentId).orElseThrow(
                () -> new NotFoundException("Tournament with id: " + tournamentId + "not found"));
    }
}
