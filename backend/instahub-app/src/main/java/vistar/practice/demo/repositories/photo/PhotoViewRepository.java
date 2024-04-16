package vistar.practice.demo.repositories.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vistar.practice.demo.models.photo.PhotoView;

import java.util.Optional;

public interface PhotoViewRepository extends JpaRepository<PhotoView, Long>, JpaSpecificationExecutor<PhotoView> {

    /**
     * Возвращает фото в ленте под номером creationOffset (нумерация начинается с 0)
     *
     * @param creationOffset Номер фотографии в ленте
     * @return Сущность фотографии
     */
    @Query("""
               SELECT P
               FROM PhotoView P
               WHERE P.isShown = true
                 AND P.userId = :ownerId
               ORDER BY P.createdAt DESC
               LIMIT 1 OFFSET :creationOffset
           """)
    Optional<PhotoView> getByCreationOffset(int creationOffset, long ownerId);
}
