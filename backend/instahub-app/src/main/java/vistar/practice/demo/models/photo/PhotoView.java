package vistar.practice.demo.models.photo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Getter
@Subselect("select * from V_PHOTO_OWNER")
public class PhotoView {

    @Id
    private long id;

    private long userId;

    private String storageUrl;

    private String iconUrl;

    private boolean isAvatar;
}
