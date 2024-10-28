package vistar.practice.demo.dtos.mail;

import lombok.Data;

@Data
public class MailMessageDto {
    String email;
    String subject;
    String message;
}
