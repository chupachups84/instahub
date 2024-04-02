package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.PhotoEntity;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    /**
     * Возвращает фото, помеченное как аватарка
     *
     * @return Аватарка или null в случае её отсутствия
     */
    PhotoEntity getByIsAvatarIsTrue();

//    boolean avatarExists();
}
