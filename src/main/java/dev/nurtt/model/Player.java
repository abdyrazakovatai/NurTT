package dev.nurtt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "players")
@Getter
@Setter
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int globalRating = 1000;

    // Гостевой игрок — рейтинг не меняется, в standings не идёт
    @Column(nullable = false)
    private boolean isGuest = false;

    @CreationTimestamp
    private Instant createdAt;

}
