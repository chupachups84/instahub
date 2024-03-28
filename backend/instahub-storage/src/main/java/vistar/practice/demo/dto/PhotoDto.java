package vistar.practice.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoDto {

    private byte[] data;
    private long ownerId;
    private final LocalDateTime creationDateTime;
    private String suffix;
}
