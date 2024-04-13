package vistar.practice.demo.repositories.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vistar.practice.demo.models.photo.PhotoView;

public interface PhotoViewRepository extends JpaRepository<PhotoView, Long>, JpaSpecificationExecutor<PhotoView> {
}
