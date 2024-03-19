package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "hashtag")
public class HashtagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sequence_generator")
    private Long id;

    private String text;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Instant createdAt;
}
