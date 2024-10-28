package vistar.practice.demo.models;


import jakarta.persistence.*;
import lombok.*;
import vistar.practice.demo.models.photo.PhotoEntity;

@Entity
@Table(name = "reactions_photos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReactionsPhotosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_by")
    private UserEntity reactionBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private PhotoEntity photo;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReactionEntity reaction;

    private Boolean isActive = true;

}
