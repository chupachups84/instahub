package vistar.practice.demo.dtos.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import vistar.practice.demo.validation.UniqueMail;
import vistar.practice.demo.validation.UniqueUsername;

import java.time.LocalDate;

@Data
@Builder
public class RegisterDto {

    @UniqueUsername
    private String username;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    private String patronymic;

    @UniqueMail
    private String email;

    private String password;

    private String bio;

    private LocalDate birthDate;

}
