package dev.nurtt.repository;

import dev.nurtt.exception.NotFoundException;
import dev.nurtt.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findById(Long id);

    default Player getPlayerById(Long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException("Player with id: " + id + " not found"));
    }

    List<Player> findAllByOrderByGlobalRatingDesc();
}
