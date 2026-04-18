package dev.nurtt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leagues")
@Getter @Setter
@NoArgsConstructor
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(nullable = false)
    private String name; // "Super Liga", "Liga A", "Liga B"...

    // Уровень: 1 = Super Liga (высший), чем больше — тем ниже
    @Column(nullable = false)
    private int level;

    // Нельзя повыситься выше
    @Column(nullable = false)
    private boolean superLeague = false;

    // Нельзя понизиться ниже
    @Column(nullable = false)
    private boolean bottomLeague = false;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<LeagueSlot> slots = new ArrayList<>();

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();
}
