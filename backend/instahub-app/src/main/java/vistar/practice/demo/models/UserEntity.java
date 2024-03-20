package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private long id;

    private String username;

    private String firstName;

    private String middleName;

    private String lastName;

    private String patronymic;

    private String email;

    @Column(name = "enc_password")
    private String encryptedPassword;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Instant createdAt;

    private boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
    private List<PhotoEntity> photos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<RepostEntity> reposts = new ArrayList<>();

    // todo -> раскомментить и сконфигурировать после создания сущности под reactions_photos
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reactionBy")
//    private List</* ||| КЛАСС СУЩНОСТИ ||| */> reactions = new ArrayList<>();

    //todo -> раскомментить после создания SubscriptionEntity
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
//    private List<SubscriptionEntity> subscriptions = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
//    private List<SubscriptionEntity> subscribers = new ArrayList<>();
}
