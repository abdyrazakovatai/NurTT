package dev.nurtt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

// 6. LeagueSlot.java
// ─────────────────────────────────────────
// Это "место игрока в лиге" — сердце системы.
// 6 слотов на каждую лигу.
// Матчи ссылаются на слоты, не на игроков напрямую.
// При замене: старый слот деактивируется, новый создаётся.
// ─────────────────────────────────────────
@Entity
@Table(name = "league_slots",
        indexes = {
                @Index(name = "idx_league_active", columnList = "league_id, active")
        })
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeagueSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // Если этот слот — замена, ссылаемся на оригинальный слот
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_for_id")
    private LeagueSlot substituteFor;

    // false = этот слот деактивирован (игрок заменён)
    @Column(nullable = false)
    private boolean active = true;

    // Место по итогам турнира (1..6), заполняется в конце
    private Integer finalPosition;

    // Статистика — обновляется после каждого ranked матча
    @Column(nullable = false)
    private int wins = 0;

    @Column(nullable = false)
    private int setsWon = 0;

    @Column(nullable = false)
    private int setsLost = 0;

    @CreationTimestamp
    private Instant joinedAt;

    // Разница партий — вычисляется на лету, не хранится в БД
    @Transient
    public int getSetsDifference() {
        return setsWon - setsLost;
    }
}
