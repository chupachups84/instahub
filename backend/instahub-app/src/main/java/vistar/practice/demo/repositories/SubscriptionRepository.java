package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vistar.practice.demo.models.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

}
