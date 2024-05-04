package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.PhotosHashtagsEntity;

import java.util.List;

@Repository
public interface PhotosHashtagsRepository extends JpaRepository<PhotosHashtagsEntity, Long> {
    List<PhotosHashtagsEntity> findByPhotoId(long id);
}
