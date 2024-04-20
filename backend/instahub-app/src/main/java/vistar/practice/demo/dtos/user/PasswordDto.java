package vistar.practice.demo.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import vistar.practice.demo.validation.SamePassword;

@Data
@Builder
public class PasswordDto {
    @JsonProperty("old_password")
    String oldPassword;

    @Size(min = 6, max = 20)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)")
    @JsonProperty("new_password")
    String newPassword;

    @NotEmpty
    @SamePassword //я думаю в дальнейшем можно будет на фронте подобные проверки делать
    @JsonProperty("confirm_new_password")
    String confirmNewPassword;
}
