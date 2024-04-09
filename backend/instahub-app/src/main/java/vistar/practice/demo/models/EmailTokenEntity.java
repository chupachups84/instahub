package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "email_token")
public class EmailTokenEntity {
    @Id
    private String id;
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder.Default
    private boolean isRevoked=false;
}
