package dev.nurtt.model;

import dev.nurtt.model.enums.PromotionDirection;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "promotion_logs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PromotionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PROMOTION (повышение) или RELEGATION (понижение)
    @Enumerated(EnumType.STRING)
    private PromotionDirection direction;

    // Место которое занял игрок (1,2 = повышение / 5,6 = понижение)
    private int finalPosition;

    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    // Откуда
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_league_id", nullable = false)
    private League fromLeague;

    // Куда
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_league_id", nullable = false)
    private League toLeague;
}