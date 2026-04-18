package dev.nurtt.repository;

import dev.nurtt.model.LeagueSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueSlotRepository extends JpaRepository<LeagueSlot, Long> {
    Long countByLeagueIdAndActiveTrue(Long leagueId);

    boolean existsByLeagueIdAndPlayerIdAndActiveTrue(Long leagueId, Long playerId);

    List<LeagueSlot> findByLeagueIdAndActiveTrueOrderByJoinedAt(Long id);

    List<LeagueSlot> findByLeagueIdAndActiveTrue(Long leagueId);

}
