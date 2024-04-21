package vistar.practice.demo.dtos.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import vistar.practice.demo.validation.UniqueMail;
import vistar.practice.demo.validation.UniqueUsername;

@Data
@Builder
public class RegisterDto {

    @Size(min = 3, max = 20)
    @UniqueUsername
    private String username;

    @NotEmpty
    @NotBlank
    @JsonProperty("first_name")
    private String firstName;

    @NotEmpty
    @NotBlank
    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    private String patronymic;

    @Email
    @NotEmpty
    @UniqueMail
    private String email;

    @Size(min = 6, max = 20)
    private String password;
}
