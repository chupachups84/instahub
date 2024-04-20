package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class PhotoStorageDto {

    @ToString.Exclude
    private byte[] data;

    private Long ownerId;
    private Boolean isAvatar;
    private String suffix;
}
