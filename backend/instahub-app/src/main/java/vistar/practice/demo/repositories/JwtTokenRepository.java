package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vistar.practice.demo.models.JwtTokenEntity;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity,Long> {
    @Query("""
            from JwtTokenEntity jwt
            join fetch jwt.user
            where jwt.user.id=:userId and (jwt.isExpired=false or jwt.isRevoked=false)
    """)
    Optional<List<JwtTokenEntity>> findAllValidTokensByUser(Long userId);

    Optional<JwtTokenEntity> findByToken(String token);

}
