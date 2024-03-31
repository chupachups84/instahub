package vistar.practice.demo.dtos.photo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PhotoStorageDto {

    private byte[] data;
    private long ownerId;
    private long photoId;
    private String suffix;
}
