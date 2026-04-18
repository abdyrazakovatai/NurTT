package dev.nurtt.model;

import dev.nurtt.model.enums.TournamentStatus;
import dev.nurtt.model.enums.TournamentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int season;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TournamentStatus status = TournamentStatus.REGISTRATION;

    @Enumerated(EnumType.STRING)
    private TournamentType tournamentType = TournamentType.ROUND_ROBIN;

    @CreationTimestamp
    private Instant createdAt;

    private Instant startedAt;
    private Instant finishedAt;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<League> leagues = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;
}
