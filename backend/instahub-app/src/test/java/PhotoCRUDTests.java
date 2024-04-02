import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.repositories.PhotoRepository;
import vistar.practice.demo.services.PhotoService;

import java.util.NoSuchElementException;

@SpringBootTest(classes = {PhotoService.class})
public class PhotoCRUDTests {

   /* @Autowired
    private PhotoService photoService;

    @MockBean
    private PhotoRepository photoRepository;

    @Test
    public void savePhoto() {
        photoService.save(PhotoDto.builder().build());
    }

    @Test
    public void getPhoto() {
        try {
            photoService.findById(1L);
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void updatePhoto() {
        try {
            photoService.update(1L, PhotoDto.builder().build());
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void deletePhoto() {
        try {
            photoService.delete(1L);
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        }
    }*/
}
