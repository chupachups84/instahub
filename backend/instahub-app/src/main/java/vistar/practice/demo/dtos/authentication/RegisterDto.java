package vistar.practice.demo.dtos.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String patronymic;
    private String email;
    private String password;
}
