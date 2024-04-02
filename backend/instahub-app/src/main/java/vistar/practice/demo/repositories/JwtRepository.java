package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vistar.practice.demo.models.JwtEntity;

import java.util.List;
import java.util.Optional;

public interface JwtRepository extends JpaRepository<JwtEntity,Long> {
    @Query("""
            from JwtEntity jwt
            join fetch jwt.user
            where jwt.user.id=:userId and jwt.isRevoked=false
    """)
    Optional<List<JwtEntity>> findAllValidTokensByUser(Long userId);

    Optional<JwtEntity> findByToken(String token);

}
