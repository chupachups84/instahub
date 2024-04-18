package vistar.practice.demo.repositories.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.photo.PhotoEntity;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    /**
     * Возвращает фото, помеченное как аватарка
     *
     * @param ownerId ID владельца фото
     * @return Аватарка или null в случае её отсутствия
     */
    @Query("""
               SELECT P
               FROM PhotoEntity P
               WHERE P.user.id = :ownerId
                 AND P.isShown = true
                 AND P.isAvatar = true
           """)
    Optional<PhotoEntity> getAvatar(long ownerId);

    Optional<PhotoEntity> findById(Long id);
}
