package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.net.URL;
import java.time.Instant;

@Entity
@Data
@Table(name = "reaction")
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private long id;

    private URL icon_url;

    @Enumerated(value = EnumType.STRING)
    private ReactionName name;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Instant createdAt;

    public enum ReactionName {
        LIKE, DISLIKE, FIRE, HEART, CRY, SUNGLASSES
    }
}
