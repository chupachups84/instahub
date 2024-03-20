package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vistar.practice.demo.models.RepostEntity;
import vistar.practice.demo.models.UserEntity;

import java.util.List;

public interface RepostRepository extends JpaRepository<RepostEntity, Long> {

    /**
     * Найти репосты по пользователю и признаку, удалены они или нет
     *
     * @param user Пользователь, сделавший репост
     * @return Список репостов пользователя
     */
    List<RepostEntity> findAllByUser(UserEntity user);

    /**
     * Найти репосты по пользователю и признаку, удалены они или нет
     *
     * @param user Пользователь, сделавший репост
     * @param isShown Репост активен и отображается
     * @return Список репостов по признакам
     */
    @Query("""
           SELECT RE
           FROM RepostEntity RE
           WHERE RE.isShown = :isShown
             AND RE.user = :user
           """
    )
    List<RepostEntity> findAllByUserAndShown(UserEntity user, boolean isShown);
}
