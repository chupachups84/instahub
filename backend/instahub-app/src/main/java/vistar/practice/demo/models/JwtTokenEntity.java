package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jwt_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JwtTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;
    private String token;

    @Builder.Default
    private boolean isRevoked = false;

    @Builder.Default
    private boolean isExpired = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
