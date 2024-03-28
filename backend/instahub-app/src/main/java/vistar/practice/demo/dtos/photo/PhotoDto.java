package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PhotoDto {

    private Long id;
    private String iconUrl;
    private String storageUrl;
    private Boolean isShown;
    private Instant createdAt;
    private Boolean isAvatar;
}
