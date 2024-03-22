package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "photos_hashtags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PhotosHashtagsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private HashtagEntity hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    private PhotoEntity photo;

    public void setHashtag(HashtagEntity hashtag) {
        //todo this.hashtag.getPhotosHashtags().add(this)
        this.hashtag = hashtag;
    }

    public void setPhoto(PhotoEntity photo) {
        // todo this.photo.getPhotosHashtags().add(this)
        this.photo = photo;
    }

    public static class PhotosHashtagsEntityBuilder {
        public PhotosHashtagsEntityBuilder hashtag(HashtagEntity hashtag) {
            this.hashtag = hashtag;
            //todo this.hashtag.getPhotosHashtags().add(this)
            return this;
        }

        public PhotosHashtagsEntityBuilder photo(PhotoEntity photo) {
            this.photo = photo;
            // todo this.photo.getPhotosHashtags().add(this)
            return this;
        }
    }
}
