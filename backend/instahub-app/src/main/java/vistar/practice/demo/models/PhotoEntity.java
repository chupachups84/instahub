package vistar.practice.demo.models;

import jakarta.persistence.*;
import lombok.*;
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

    private boolean isShown = true;

    private LocalDateTime createdAt;

    private boolean isAvatar = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "photo")
    List<CommentEntity> comments = new ArrayList<>();
}
