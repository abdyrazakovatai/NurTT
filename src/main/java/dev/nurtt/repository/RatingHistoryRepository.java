package dev.nurtt.repository;

import dev.nurtt.model.RatingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingHistoryRepository extends JpaRepository<RatingHistory, Long> {
    List<RatingHistory> findByPlayerIdOrderByCreatedAtDesc(Long playerId);
}
