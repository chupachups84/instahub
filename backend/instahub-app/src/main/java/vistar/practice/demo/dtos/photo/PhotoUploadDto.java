package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class PhotoUploadDto {

    private MultipartFile file;
    private String description;
    private List<String> hashtags;
    private Boolean isAvatar;
}
