package vistar.practice.demo.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {

    @NotEmpty
    @NotBlank
    private String username;

    @Size(min = 6, max = 20)
    private String password;
}
