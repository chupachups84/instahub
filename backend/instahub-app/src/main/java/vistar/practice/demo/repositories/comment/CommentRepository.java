package vistar.practice.demo.repositories.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.comment.CommentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
               SELECT C
               FROM CommentEntity C
               WHERE C.photo.id = :photoId
                 AND C.isShown = true
                 AND C.createdAt <= :lastCommentCreationTime
               ORDER BY C.createdAt DESC
               LIMIT 1 OFFSET :commentOffset
           """)
    Optional<CommentEntity> findByOffset(LocalDateTime lastCommentCreationTime, long photoId, long commentOffset);
}
