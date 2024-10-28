package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.DirectMessageEntity;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessageEntity, Long> {
}
