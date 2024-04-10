package vistar.practice.demo.dtos.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {

    @Size(min = 3, max = 20)
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
    private String email;

    @Size(min = 6, max = 20)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)")
    private String password;
}
