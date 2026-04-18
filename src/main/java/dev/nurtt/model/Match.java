package dev.nurtt.model;

import dev.nurtt.model.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "matches",
        indexes = {
                @Index(name = "idx_league", columnList = "league_id"),
                @Index(name = "idx_status", columnList = "status")
        })
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    // Слот 1 (не игрок напрямую!)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot1_id", nullable = false)
    private LeagueSlot slot1;

    // Слот 2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot2_id", nullable = false)
    private LeagueSlot slot2;

    // Победитель (null пока матч не сыгран)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_slot_id")
    private LeagueSlot winnerSlot;

    // Счёт по сетам: 3:0, 3:1, 3:2
    @Column(nullable = false)
    private int slot1Sets = 0;

    @Column(nullable = false)
    private int slot2Sets = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    // false = гостевой матч, рейтинг не меняется, в standings не идёт
    @Column(nullable = false)
    private boolean ranked = true;

    private Instant playedAt;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setNumber ASC")
    private List<SetResult> sets = new ArrayList<>();

    // Проигравший слот — вычисляется на лету
    @Transient
    public LeagueSlot getLoserSlot() {
        if (winnerSlot == null) return null;
        return winnerSlot.getId().equals(slot1.getId()) ? slot2 : slot1;
    }
}
