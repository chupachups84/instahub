package vistar.practice.demo.repositories.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.comment.CommentView;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CommentViewRepository extends JpaRepository<CommentView, Long>, JpaSpecificationExecutor<CommentView> {

    @Query("""
               SELECT C
               FROM CommentView C
               WHERE C.photoId = :photoId
                 AND C.isShown = true
                 AND C.createdAt <= :lastCommentCreationTime
               ORDER BY C.createdAt DESC
               LIMIT 1 OFFSET :commentOffset
           """)
    Optional<CommentView> findByOffset(LocalDateTime lastCommentCreationTime, long photoId, long commentOffset);


    @Query("""
               SELECT C
               FROM CommentView C
               WHERE C.photoId = :photoId
                 AND C.isShown = true
               ORDER BY C.createdAt DESC
               LIMIT 1 OFFSET 0
           """)
    Optional<CommentView> getLastShownCommentByPhotoId(long photoId);
}
