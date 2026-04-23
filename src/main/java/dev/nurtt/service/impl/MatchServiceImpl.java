package dev.nurtt.service.impl;

import dev.nurtt.dto.request.MatchResultRequest;
import dev.nurtt.dto.request.SetRequest;
import dev.nurtt.dto.response.MatchResponse;
import dev.nurtt.dto.response.SetResponse;
import dev.nurtt.exception.BadRequestException;
import dev.nurtt.model.LeagueSlot;
import dev.nurtt.model.Match;
import dev.nurtt.model.SetResult;
import dev.nurtt.model.enums.MatchStatus;
import dev.nurtt.repository.LeagueSlotRepository;
import dev.nurtt.repository.MatchRepository;
import dev.nurtt.repository.SetResultRepository;
import dev.nurtt.service.MatchService;
import dev.nurtt.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository      matchRepository;
    private final LeagueSlotRepository leagueSlotRepository;
    private final SetResultRepository  setResultRepository;
    private final RatingService        ratingService;

    // ─────────────────────────────────────
    // Ввод результата матча
    // ─────────────────────────────────────
    @Override
    public MatchResponse submitResult(Long matchId, MatchResultRequest request) {
        Match match = matchRepository.getMatchById(matchId);

        // Матч уже сыгран?
        if (match.getStatus() == MatchStatus.COMPLETED) {
            throw new BadRequestException("Match already completed");
        }

        // Валидация партий (best of 5 — до 3 побед)
        validateSets(request.getSets());

        // Подсчитать сеты каждого слота
        int slot1Sets = 0;
        int slot2Sets = 0;

        for (SetRequest set : request.getSets()) {
            if (set.getScore1() > set.getScore2()) {
                slot1Sets++;
            } else {
                slot2Sets++;
            }
        }

        // Определить победителя по winnerId
        LeagueSlot winner;
        LeagueSlot loser;

        if (request.getWinnerSlotId().equals(match.getSlot1().getId())) {
            winner = match.getSlot1();
            loser  = match.getSlot2();
        } else if (request.getWinnerSlotId().equals(match.getSlot2().getId())) {
            winner = match.getSlot2();
            loser  = match.getSlot1();
        } else {
            throw new BadRequestException("Winner id does not match any slot in this match");
        }

        // Дополнительная проверка — победитель набрал 3 сета?
        int winnerSets = winner.getId().equals(match.getSlot1().getId()) ? slot1Sets : slot2Sets;
        if (winnerSets != 3) {
            throw new BadRequestException("Winner must have exactly 3 sets won");
        }

        // Сохранить партии
        List<SetResult> setResults = new ArrayList<>();
        for (int i = 0; i < request.getSets().size(); i++) {
            SetRequest s = request.getSets().get(i);
            SetResult sr = new SetResult();
            sr.setMatch(match);
            sr.setSetNumber(i + 1);
            sr.setScore1(s.getScore1());
            sr.setScore2(s.getScore2());
            setResults.add(sr);
        }
        setResultRepository.saveAll(setResults);

        // Обновить матч
        match.setSlot1Sets(slot1Sets);
        match.setSlot2Sets(slot2Sets);
        match.setWinnerSlot(winner);
        match.setStatus(MatchStatus.COMPLETED);
        match.setPlayedAt(Instant.now());
        matchRepository.save(match);

        // Обновить статистику слотов (только если ranked)
        if (match.isRanked()) {
            updateSlotStats(winner, loser, slot1Sets, slot2Sets,
                    winner.getId().equals(match.getSlot1().getId()));

            // Пересчитать рейтинг игроков
            ratingService.recalculate(match);
        }

        return buildResponse(match, setResults);
    }

    // ─────────────────────────────────────
    // Валидация партий
    // best of 5: до 3 побед, максимум 5 партий
    // ─────────────────────────────────────
    private void validateSets(List<SetRequest> sets) {
        if (sets == null || sets.isEmpty()) {
            throw new BadRequestException("Sets cannot be empty");
        }
        if (sets.size() > 5) {
            throw new BadRequestException("Maximum 5 sets allowed");
        }

        int p1 = 0, p2 = 0;
        for (SetRequest s : sets) {
            if (s.getScore1() == s.getScore2()) {
                throw new BadRequestException("A set cannot end in a draw");
            }
            if (s.getScore1() > s.getScore2()) p1++;
            else p2++;
        }

        // Один из игроков должен набрать ровно 3
        if (p1 != 3 && p2 != 3) {
            throw new BadRequestException("Match must have a winner with 3 sets");
        }

        // Матч не должен продолжаться после победы
        // Например [3:0] — нельзя добавить 4-ю партию
        if (p1 == 3 && p2 == 3) {
            throw new BadRequestException("Both players cannot have 3 sets");
        }
    }

    // ─────────────────────────────────────
    // Обновить статистику слотов
    // ─────────────────────────────────────
    private void updateSlotStats(LeagueSlot winner, LeagueSlot loser,
                                 int slot1Sets, int slot2Sets, boolean slot1Won) {
        int winnerSetsWon  = slot1Won ? slot1Sets : slot2Sets;
        int winnerSetsLost = slot1Won ? slot2Sets : slot1Sets;
        int loserSetsWon   = slot1Won ? slot2Sets : slot1Sets;
        int loserSetsLost  = slot1Won ? slot1Sets : slot2Sets;

        // Победитель
        winner.setWins(winner.getWins() + 1);
        winner.setSetsWon(winner.getSetsWon() + winnerSetsWon);
        winner.setSetsLost(winner.getSetsLost() + winnerSetsLost);

        // Проигравший
        loser.setSetsWon(loser.getSetsWon() + loserSetsWon);
        loser.setSetsLost(loser.getSetsLost() + loserSetsLost);

        leagueSlotRepository.save(winner);
        leagueSlotRepository.save(loser);
    }

    // ─────────────────────────────────────
    // Получить матч
    // ─────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public MatchResponse getMatch(Long matchId) {
        Match match = matchRepository.getMatchById(matchId);
        List<SetResult> sets = setResultRepository.findByMatchIdOrderBySetNumber(matchId);
        return buildResponse(match, sets);
    }

    // ─────────────────────────────────────
    // Получить все матчи лиги
    // ─────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getMatchesByLeague(Long leagueId) {
        List<Match> matches = matchRepository.findByLeagueIdOrderById(leagueId);
        return matches.stream()
                .map(m -> buildResponse(m,
                        setResultRepository.findByMatchIdOrderBySetNumber(m.getId())))
                .toList();
    }

    // ─────────────────────────────────────
    // Построить response
    // ─────────────────────────────────────
    private MatchResponse buildResponse(Match match, List<SetResult> sets) {
        List<SetResponse> setResponses = sets.stream()
                .map(s -> SetResponse.builder()
                        .setNumber(s.getSetNumber())
                        .score1(s.getScore1())
                        .score2(s.getScore2())
                        .build())
                .toList();

        return MatchResponse.builder()
                .matchId(match.getId())
                .slot1Id(match.getSlot1().getId())
                .slot2Id(match.getSlot2().getId())
                .player1Name(match.getSlot1().getPlayer().getFullName())
                .player2Name(match.getSlot2().getPlayer().getFullName())
                .winnerName(match.getWinnerSlot() != null
                        ? match.getWinnerSlot().getPlayer().getFullName() : null)
                .slot1Sets(match.getSlot1Sets())
                .slot2Sets(match.getSlot2Sets())
                .status(match.getStatus())
                .isRanked(match.isRanked())
                .sets(setResponses)
                .build();
    }
}
