package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoStorageDto {

    private byte[] data;
    private Long ownerId;
    private Boolean isAvatar;
    private String suffix;
}
