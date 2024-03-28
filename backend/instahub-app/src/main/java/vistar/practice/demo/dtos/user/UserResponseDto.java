package vistar.practice.demo.dtos.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponseDto {
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String patronymic;
    private String email;
    private Instant createdAt;
}