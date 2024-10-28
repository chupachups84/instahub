package vistar.practice.demo.repositories;

import org.springframework.data.domain.Pageable;
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
    List<SubscriptionEntity> findAllByUser(UserEntity user, Pageable pageable);
    List<SubscriptionEntity> findAllBySubscriber(UserEntity subscriber, Pageable pageable);
    Optional<SubscriptionEntity> findBySubscriberAndUser(UserEntity subscriber,UserEntity user);
    Long countAllByUserAndIsActiveIsTrue(UserEntity user);
    Long countAllBySubscriberAndIsActiveIsTrue(UserEntity subscriber);

    @Query("""
               SELECT exists(
                   SELECT P
                   FROM SubscriptionEntity P
                   WHERE P.subscriber.id = :userId
                     AND P.user.id = :relatedUserId
                     AND P.isActive = true
               )
           """)
    boolean isSubscribed(long userId, long relatedUserId);

    @Query("""
                  SELECT P
                  FROM SubscriptionEntity P
                  WHERE P.subscriber.id = :subscriberId
                    AND P.user.id = :userId
           """)
    Optional<SubscriptionEntity> getSubscription(Long subscriberId, Long userId);
}
