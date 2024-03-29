package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class PhotoUploadDto {

    private MultipartFile file;
    private Long ownerId;
    private Boolean isAvatar;
}
