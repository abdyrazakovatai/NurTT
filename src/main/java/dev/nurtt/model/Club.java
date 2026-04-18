package dev.nurtt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubs")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@ToString
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Tournament> tournaments = new ArrayList<>();
}