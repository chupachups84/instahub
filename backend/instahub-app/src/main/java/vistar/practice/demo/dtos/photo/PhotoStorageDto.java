package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoStorageDto {

    private byte[] data;
    private long ownerId;
    private long photoId;
    private String suffix;
}
