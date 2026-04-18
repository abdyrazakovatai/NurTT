package dev.nurtt.service.impl;

import dev.nurtt.dto.response.StandingResponse;
import dev.nurtt.model.LeagueSlot;
import dev.nurtt.model.Match;
import dev.nurtt.model.enums.MatchStatus;
import dev.nurtt.repository.LeagueSlotRepository;
import dev.nurtt.repository.MatchRepository;
import dev.nurtt.service.StandingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StandingsServiceImpl implements StandingsService {

    private final LeagueSlotRepository slotRepository;
    private final MatchRepository matchRepository;

    @Override
    public List<StandingResponse> getStandings(Long leagueId) {
        // Получить все активные слоты лиги
        List<LeagueSlot> slots = slotRepository.findByLeagueIdAndActiveTrue(leagueId);

        // Получить все завершённые ranked матчи лиги
        List<Match> completedMatches = matchRepository
                .findByLeagueIdAndStatusAndRankedTrue(leagueId, MatchStatus.COMPLETED);

        // Построить map для head-to-head (личные матчи)
        // ключ: "slotId1_slotId2" (нормализованный) → id победившего слота
        Map<String, Long> headToHead = buildHeadToHead(completedMatches);

        // Сортировка:
        // 1. wins (больше — выше)
        // 2. setsDiff (больше — выше)
        // 3. head-to-head (личный матч)
        List<LeagueSlot> sorted = slots.stream()
                .sorted((a, b) -> {
                    // 1. Победы
                    if (b.getWins() != a.getWins()) {
                        return Integer.compare(b.getWins(), a.getWins());
                    }
                    // 2. Разница партий
                    if (b.getSetsDifference() != a.getSetsDifference()) {
                        return Integer.compare(b.getSetsDifference(), a.getSetsDifference());
                    }
                    // 3. Личный матч
                    return headToHeadCompare(a, b, headToHead);
                })
                .toList();

        // Назначить места и построить response
        List<StandingResponse> standings = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            LeagueSlot slot = sorted.get(i);
            int matchesPlayed = slot.getWins() + getLosses(slot, completedMatches);

            standings.add(StandingResponse.builder()
                    .place(i + 1)
                    .slotId(slot.getId())
                    .playerId(slot.getPlayer().getId())
                    .playerName(slot.getPlayer().getFullName())
                    .wins(slot.getWins())
                    .losses(getLosses(slot, completedMatches))
                    .setsWon(slot.getSetsWon())
                    .setsLost(slot.getSetsLost())
                    .setsDiff(slot.getSetsDifference())
                    .matchesPlayed(matchesPlayed)
                    .build());
        }

        return standings;
    }

    // ─────────────────────────────────────
    // Head-to-head сравнение
    // Смотрим личный матч между двумя игроками
    // ─────────────────────────────────────
    private int headToHeadCompare(LeagueSlot a, LeagueSlot b,
                                  Map<String, Long> headToHead) {
        String key = pairKey(a.getId(), b.getId());
        Long winnerId = headToHead.get(key);

        if (winnerId == null) return 0; // матч ещё не сыгран

        // Если a выиграл личный матч → a выше (возвращаем -1)
        return winnerId.equals(a.getId()) ? -1 : 1;
    }

    // ─────────────────────────────────────
    // Построить map head-to-head
    // ─────────────────────────────────────
    private Map<String, Long> buildHeadToHead(List<Match> matches) {
        Map<String, Long> map = new HashMap<>();
        for (Match match : matches) {
            if (match.getWinnerSlot() == null) continue;
            String key = pairKey(
                    match.getSlot1().getId(),
                    match.getSlot2().getId()
            );
            map.put(key, match.getWinnerSlot().getId());
        }
        return map;
    }

    // ─────────────────────────────────────
    // Нормализованный ключ пары
    // Чтобы "1_2" и "2_1" был один ключ
    // ─────────────────────────────────────
    private String pairKey(Long id1, Long id2) {
        Long lo = Math.min(id1, id2);
        Long hi = Math.max(id1, id2);
        return lo + "_" + hi;
    }

    // ─────────────────────────────────────
    // Подсчёт поражений слота
    // ─────────────────────────────────────
    private int getLosses(LeagueSlot slot, List<Match> matches) {
        int losses = 0;
        for (Match match : matches) {
            boolean participated =
                    match.getSlot1().getId().equals(slot.getId()) ||
                            match.getSlot2().getId().equals(slot.getId());

            boolean lost = match.getWinnerSlot() != null &&
                    !match.getWinnerSlot().getId().equals(slot.getId());

            if (participated && lost) losses++;
        }
        return losses;
    }
}

