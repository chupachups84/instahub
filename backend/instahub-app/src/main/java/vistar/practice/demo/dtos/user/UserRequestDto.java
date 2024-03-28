package vistar.practice.demo.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String patronymic;
    private String email;
    private String password;
}
