package vistar.practice.demo.dtos.photo;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PhotoStorageDto {

    private byte[] data;
    private long ownerId;
    private long photoId;
    private LocalDateTime creationDateTime;
    private String suffix;
}
