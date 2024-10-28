package vistar.practice.demo.repositories.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vistar.practice.demo.models.photo.PhotoView;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PhotoViewRepository extends JpaRepository<PhotoView, Long>, JpaSpecificationExecutor<PhotoView> {

    /**
     * Возвращает фото в профиле под номером creationOffset (нумерация начинается с 0)
     *
     * @param creationOffset Номер фотографии в профиле
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
    Optional<PhotoView> getByIconCreationOffset(int creationOffset, long ownerId);


    /**
     * Возвращает фото в ленте под номером creationOffset (нумерация начинается с 0)
     *
     * @param creationOffset Номер фотографии в ленте
     * @param ownerId ID владельца фото
     * @param lastPhotoCreationTime Время загрузки последней фотографии
     * @return Сущность фотографии
     */
    @Query("""
               SELECT P
               FROM PhotoView P
               LEFT JOIN SubscriptionEntity S on S.subscriber.id = :ownerId
               WHERE P.isShown = true
                 AND S.isActive = true
                 AND P.userId = S.user.id
                 AND P.createdAt <= :lastPhotoCreationTime
               ORDER BY P.createdAt DESC
               LIMIT 1 OFFSET :creationOffset
           """)
    Optional<PhotoView> getByFeedCreationOffset(int creationOffset, long ownerId, LocalDateTime lastPhotoCreationTime);

    @Query("""
                SELECT P
                FROM PhotoView P
                WHERE P.isShown = true
                  AND P.userId = :ownerId
                  AND P.isAvatar = true
           """)
    Optional<PhotoView> getAvatar(long ownerId);

    @Query("""
               SELECT count(P)
               FROM PhotoView P
               WHERE P.isShown = true
                 AND P.username = :username
           """)
    Integer countByUsername(String username);


    @Query("""
               SELECT count(P)
               FROM PhotoView P
               WHERE P.isShown = true
                 AND P.userId = :userId
           """)
    Integer countByUserId(Long userId);
}
