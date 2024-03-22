package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "hashtag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HashtagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    private String text;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;
}
