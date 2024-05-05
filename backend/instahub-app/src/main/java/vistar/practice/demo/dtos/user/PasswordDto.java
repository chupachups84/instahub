package vistar.practice.demo.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class PasswordDto {
    @JsonProperty("old_password")
    String oldPassword;

    @JsonProperty("new_password")
    String newPassword;

}
