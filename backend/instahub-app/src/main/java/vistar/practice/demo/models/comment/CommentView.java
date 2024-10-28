package vistar.practice.demo.models.comment;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import vistar.practice.demo.models.UserEntity;

import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Subselect("select * from V_COMMENT_OWNER")
public class CommentView {

    @Id
    private Long id;

    private String text;

    private Boolean isShown = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    private Long photoId;

    private String userFirstName;

    private String userMiddleName;

    private String userLastName;

    public String getUserFullName() {
        return userFirstName + " " + (userMiddleName != null ? userMiddleName + " " : "") + userLastName;
    }
}

