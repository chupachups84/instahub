package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    private String username;

    private String firstName;

    private String middleName;

    private String lastName;

    private String patronymic;

    private String email;

    private String password;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @Builder.Default
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<PhotoEntity> photos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<RepostEntity> reposts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reactionBy")
    private List<ReactionsPhotosEntity> reactions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "subscriber")
    private List<SubscriptionEntity> subscribers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sender")
    private List<DirectMessageEntity> sentMessages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "receiver")
    private List<DirectMessageEntity> receivedMessages = new ArrayList<>();

    public void addPhoto(PhotoEntity photoEntity) {
        Objects.requireNonNull(photoEntity);
        this.photos.add(photoEntity);
        photoEntity.setUser(this);
    }
}
