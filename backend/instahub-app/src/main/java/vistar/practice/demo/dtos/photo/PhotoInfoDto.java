package vistar.practice.demo.dtos.photo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PhotoInfoDto {

    private Long id;
    private Long ownerId;
    private String description;
    private List<String> hashtags;
    private String iconUrl;
    private String storageUrl;
    private String feedUrl;
    private Boolean isShown;
    private LocalDateTime createdAt;
    private Boolean isAvatar;
}
