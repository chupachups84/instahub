package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vistar.practice.demo.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);
}
