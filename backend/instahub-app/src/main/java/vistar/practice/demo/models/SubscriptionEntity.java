package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "subscription")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant subscribedAt;

    @Column(name = "sub_is_active")
    @Builder.Default
    private boolean isActive = true;
}
