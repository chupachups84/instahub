package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.ReactionsPhotosEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.models.photo.PhotoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionsPhotosRepository extends JpaRepository<ReactionsPhotosEntity, Long> {
    List<ReactionsPhotosEntity> findAllByPhoto(PhotoEntity photo);
    Optional<ReactionsPhotosEntity> findByReactionByAndPhoto(UserEntity reactionBy, PhotoEntity photo_id);
}
