package vistar.practice.demo.dtos.photo;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PhotoStorageDto {

    private byte[] data;
    private Long ownerId;
    private String description;
    private List<String> hashtags;
    private Boolean isAvatar;
    private String suffix;
}
