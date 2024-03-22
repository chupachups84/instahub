package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "direct_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DirectMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity receiver;

    private String text;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant postedAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;

    @Builder.Default
    private boolean isShown = true;

}