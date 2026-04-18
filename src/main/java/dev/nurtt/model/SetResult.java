package dev.nurtt.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "set_results",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "set_number"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SetResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Номер партии: 1, 2, 3, 4, 5
    private int setNumber;

    // Счёт первого слота в этой партии
    private int score1;

    // Счёт второго слота в этой партии
    private int score2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
}