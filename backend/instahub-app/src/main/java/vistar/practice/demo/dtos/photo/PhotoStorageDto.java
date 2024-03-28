package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PhotoStorageDto {

    private byte[] data;
    private long ownerId;
    private final LocalDateTime creationDateTime = LocalDateTime.now();
    private String suffix;
}
