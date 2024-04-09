package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vistar.practice.demo.dtos.mail.MailMessageDto;
import vistar.practice.demo.kafka.KafkaSender;

@Service
@RequiredArgsConstructor
public class MailService {
    private final KafkaSender kafkaSender;
    @Value("${kafka.topic.mail}")
    private String topic;
    @Value("${domain.address}")
    private String domain;
    @Value("${auth.uri}")
    private String uri;
    public void sendConfirmationTokenMessage(String email, String token){
        kafkaSender.sendTransactionalMailMessage(
                topic,
                MailMessageDto.builder()
                        .email(email)
                        .subject("Confirmation message")
                        .message("Click on the link to confirm your email "+domain+"/"+uri+"?token="+token)
                        .build()
        );
    }
}
