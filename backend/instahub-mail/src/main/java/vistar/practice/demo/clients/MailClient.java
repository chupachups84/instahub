package vistar.practice.demo.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vistar.practice.demo.dtos.mail.MailMessageDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailClient {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;

    @KafkaListener(
            topics = "${kafka.topic.mail}"
    )
    public void sendMailMessage(MailMessageDto message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getMessage());
        mailSender.send(mailMessage);
        log.info("sending message to {}",message.getEmail());
    }
}
