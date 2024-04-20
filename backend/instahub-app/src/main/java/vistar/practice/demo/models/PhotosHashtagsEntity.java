package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import vistar.practice.demo.models.photo.PhotoEntity;

@Entity
@Table(name = "photos_hashtags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PhotosHashtagsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private HashtagEntity hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    private PhotoEntity photo;

}
