package vistar.practice.demo.dtos.photo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoUploadDto {

    private MultipartFile file;
    private Long ownerId;
    private Boolean isAvatar;
}
