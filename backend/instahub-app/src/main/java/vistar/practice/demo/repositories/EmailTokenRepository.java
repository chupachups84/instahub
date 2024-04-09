package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.EmailTokenEntity;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailTokenEntity, String> {
}
