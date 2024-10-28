package vistar.practice.demo.models.photo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Subselect("select * from V_PHOTO_OWNER")
public class PhotoView {

    @Id
    private long id;

    private long userId;

    private String description;

    private String storageUrl;

    private String iconUrl;

    private String feedUrl;

    private boolean isAvatar;

    private LocalDateTime createdAt;

    private boolean isShown;

    private String username;

    private String userFirstName;

    private String userMiddleName;

    private String userLastName;

    public String getUserFullName() {
        return userFirstName + " " +
                (userMiddleName != null && !userMiddleName.isEmpty() ? userMiddleName + " " : "") +
                userLastName;
    }
}
