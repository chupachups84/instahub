package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vistar.practice.demo.models.DirectMessageEntity;

public interface DirectMessageRepository extends JpaRepository<DirectMessageEntity, Long> {

}
