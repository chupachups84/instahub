package vistar.practice.demo.dtos.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailMessageDto {

    @Email
    String email;
    String subject;

    @NotEmpty
    String message;
}
