package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.RepostEntity;

@Repository
public interface RepostRepository extends JpaRepository<RepostEntity, Long> {

    @Query("""
               SELECT count(r)
               FROM RepostEntity r
               WHERE r.photo.id = :photoId
                 AND r.isShown = true
           """)
    int countByPhotoId(long photoId);
}
