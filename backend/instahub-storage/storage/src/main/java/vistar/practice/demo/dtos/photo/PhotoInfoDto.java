package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PhotoInfoDto {

    private Long id;
    private Long ownerId;
    private String iconUrl;
    private String storageUrl;
    private String feedUrl;
    private Boolean isShown;

    private String description;
    private List<String> hashtags;

    private LocalDateTime createdAt;
    private Boolean isAvatar;
}
