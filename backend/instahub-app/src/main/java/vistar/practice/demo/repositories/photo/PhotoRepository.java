package vistar.practice.demo.repositories.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.photo.PhotoEntity;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    /**
     * Возвращает фото, помеченное как аватарка
     *
     * @return Аватарка или null в случае её отсутствия
     */
    Optional<PhotoEntity> getByIsAvatarIsTrue();

}
