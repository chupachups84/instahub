package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vistar.practice.demo.models.ReactionsPhotosEntity;

@Repository
public interface ReactionsPhotosRepository extends JpaRepository<ReactionsPhotosEntity, Long> {
}
