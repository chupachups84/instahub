package vistar.practice.demo.dtos.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String patronymic;
    private String email;
    private Instant createdAt;
    private String bio;
    private LocalDate birthDate;
    private Integer postCount;
}