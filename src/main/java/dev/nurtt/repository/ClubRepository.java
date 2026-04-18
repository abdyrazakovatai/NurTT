package dev.nurtt.repository;

import dev.nurtt.exception.NotFoundException;
import dev.nurtt.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findById(Long id);

    default Club getClubById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Club with id: " + id));
    }
}
