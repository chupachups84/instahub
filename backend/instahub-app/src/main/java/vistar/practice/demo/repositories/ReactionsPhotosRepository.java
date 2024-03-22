package vistar.practice.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vistar.practice.demo.models.ReactionsPhotosEntity;

public interface ReactionsPhotosRepository extends JpaRepository<ReactionsPhotosEntity,Long> {
}
