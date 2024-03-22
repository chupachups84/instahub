package vistar.practice.demo.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Table(name = "reactions_photos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReactionsPhotosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PhotoEntity photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_by")
    private UserEntity reactionBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReactionEntity reaction;

    public static ReactionsPhotosEntityBuilder builder() {
        return new ReactionsPhotosEntityBuilder();
    }

    public void setPhoto(PhotoEntity photo) {
        // todo this.photo.ReactionsPhotos().add(this);
        this.photo = photo;
    }

    public void setReactionBy(UserEntity reactionBy) {
        //todo this.reactionBy.ReactionsPhotos().add(this);
        this.reactionBy = reactionBy;
    }

    public static class ReactionsPhotosEntityBuilder {

        public ReactionsPhotosEntityBuilder photo(PhotoEntity photo) {
            // todo this.photo.ReactionsPhotos().add(this);
            this.photo = photo;
            return this;
        }

        public ReactionsPhotosEntityBuilder reactionBy(UserEntity reactionBy) {
            // todo this.reactionBy.ReactionsPhotos().add(this);
            this.reactionBy = reactionBy;
            return this;
        }

    }
}
