package vistar.practice.demo.dtos.mail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailMessageDto {
    String email;
    String subject;
    String message;
}
