package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "reaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ReactionName name;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

}
