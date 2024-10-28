package vistar.practice.demo.dtos.photo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
public class PhotoStorageDto {

    @ToString.Exclude
    private byte[] data;

    private Long ownerId;
    private String description;
    private List<String> hashtags;
    private Boolean isAvatar;
    private String suffix;
}
