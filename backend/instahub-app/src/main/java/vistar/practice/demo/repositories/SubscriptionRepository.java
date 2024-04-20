package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.SubscriptionEntity;
import vistar.practice.demo.models.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("""
               SELECT S
               FROM SubscriptionEntity S
               WHERE S.subscriber=:user
                 AND S.isActive=true
           """)
    List<SubscriptionEntity> findAllActiveSubscriptions(UserEntity user);

    List<SubscriptionEntity> findAllByUser(UserEntity user);
    Optional<SubscriptionEntity> findBySubscriberAndUser(UserEntity subscriber,UserEntity user);

}
