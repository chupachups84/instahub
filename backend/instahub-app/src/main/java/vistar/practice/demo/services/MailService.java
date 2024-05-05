package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vistar.practice.demo.dtos.mail.MailMessageDto;
import vistar.practice.demo.kafka.producers.KafkaSender;
import vistar.practice.demo.models.EmailTokenEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.EmailTokenRepository;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MailService {
    private final KafkaSender kafkaSender;
    private final EmailTokenRepository emailTokenRepository;
    @Value("${kafka.topic.mail}")
    private String topic;
    @Value("${frontend.activate}")
    private String activateUrl;

    public void saveEmailToken(UserEntity user, String token,Long expiration) {
        emailTokenRepository.save(
                EmailTokenEntity.builder()
                        .id(token)
                        .expiresAt(Instant.now().plus(Duration.ofMillis(expiration)))
                        .user(user).build()
        );
    }


    public void sendActivationAccountMessage(String email, String token) {
        kafkaSender.sendTransactionalMailMessage(
                topic,
                MailMessageDto.builder()
                        .email(email)
                        .subject("Account Activation Message")
                        .message("Click on the link to activate your account " + activateUrl + "?token=" + token)
                        .build()
        );
    }
}
