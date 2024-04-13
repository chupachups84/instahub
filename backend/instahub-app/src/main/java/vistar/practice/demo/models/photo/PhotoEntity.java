package vistar.practice.demo.models.photo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import vistar.practice.demo.models.CommentEntity;
import vistar.practice.demo.models.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    private Long id;

    private String iconUrl;

    private String storageUrl;

    @Builder.Default
    private boolean isShown = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    private boolean isAvatar = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "photo")
    List<CommentEntity> comments = new ArrayList<>();
}
