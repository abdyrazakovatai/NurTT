package dev.nurtt.repository;

import dev.nurtt.model.SetResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetResultRepository extends JpaRepository<SetResult, Long> {
    List<SetResult> findByMatchIdOrderBySetNumber(Long matchId);
}
