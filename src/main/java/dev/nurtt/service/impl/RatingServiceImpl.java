package dev.nurtt.service.impl;

import dev.nurtt.model.Match;
import dev.nurtt.model.Player;
import dev.nurtt.model.RatingHistory;
import dev.nurtt.repository.PlayerRepository;
import dev.nurtt.repository.RatingHistoryRepository;
import dev.nurtt.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final PlayerRepository playerRepository;
    private final RatingHistoryRepository ratingHistoryRepository;

    @Override
    public void recalculate(Match match) {
        // Матч не ранкед — рейтинг не меняется
        if (!match.isRanked()) return;

        Player winner = match.getWinnerSlot().getPlayer();
        Player loser  = match.getLoserSlot().getPlayer();

        // Гость — рейтинг не меняется
        if (winner.isGuest() || loser.isGuest()) return;

        int Rv = winner.getGlobalRating();  // рейтинг победителя
        int Rp = loser.getGlobalRating();   // рейтинг проигравшего

        double k = getK(match);  // коэффициент турнира
        double D = getD(match);  // коэффициент счёта

        // Δ = [(100 − (Rв − Rп)) / 10] × k × D
        double delta = ((100.0 - (Rv - Rp)) / 10.0) * k * D;

        // Минимум 1 очко за победу
        int deltaInt = (int) Math.max(1, Math.round(delta));

        // Применить дельту
        applyDelta(winner, match, +deltaInt);
        applyDelta(loser,  match, -deltaInt);
    }

    // ─────────────────────────────────────
    // k — коэффициент турнира
    // Зависит от уровня лиги
    // ─────────────────────────────────────
    private double getK(Match match) {
        int level = match.getLeague().getLevel();
        return switch (level) {
            case 1 -> 1.5;  // Super Liga
            case 2 -> 1.2;  // Liga A
            case 3 -> 1.0;  // Liga B
            default -> 0.8; // Liga C и ниже
        };
    }

    // ─────────────────────────────────────
    // D — коэффициент счёта
    // Зависит от разницы сетов
    // 3:0 → D = 1.0 (разгром)
    // 3:1 → D = 0.9
    // 3:2 → D = 0.8 (упорный матч)
    // ─────────────────────────────────────
    private double getD(Match match) {
        int loserSets = Math.min(match.getSlot1Sets(), match.getSlot2Sets());
        return switch (loserSets) {
            case 0 -> 1.0;  // 3:0
            case 1 -> 0.9;  // 3:1
            case 2 -> 0.8;  // 3:2
            default -> 0.8;
        };
    }

    // ─────────────────────────────────────
    // Применить дельту к игроку
    // Сохранить в историю
    // ─────────────────────────────────────
    private void applyDelta(Player player, Match match, int delta) {
        int oldRating = player.getGlobalRating();

        // Рейтинг не может быть меньше 0
        int newRating = Math.max(0, oldRating + delta);
        player.setGlobalRating(newRating);
        playerRepository.save(player);

        // Сохранить в историю
        RatingHistory history = new RatingHistory();
        history.setPlayer(player);
        history.setMatch(match);
        history.setOldRating(oldRating);
        history.setNewRating(newRating);
        history.setDelta(delta);
        ratingHistoryRepository.save(history);
    }
}
