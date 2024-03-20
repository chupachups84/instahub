package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vistar.practice.demo.models.ReactionEntity;

public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
}
