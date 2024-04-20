package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.CommentEntity;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
               SELECT C
               FROM CommentEntity C
               WHERE C.photo.id = :photoId
                 AND C.isShown = true
               ORDER BY C.createdAt DESC
               LIMIT 1 OFFSET 0
           """)
    Optional<CommentEntity> getLastShownCommentByPhotoId(long photoId);
}
