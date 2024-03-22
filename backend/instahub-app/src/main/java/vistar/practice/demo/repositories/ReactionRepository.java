package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.ReactionEntity;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
}
