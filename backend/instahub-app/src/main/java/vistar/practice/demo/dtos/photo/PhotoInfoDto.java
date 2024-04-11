package vistar.practice.demo.dtos.photo;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PhotoInfoDto {

    private Long id;
    private Long ownerId;
    private String iconUrl;
    private String storageUrl;
    private Boolean isShown;
    private LocalDateTime createdAt;
    private Boolean isAvatar;
}
