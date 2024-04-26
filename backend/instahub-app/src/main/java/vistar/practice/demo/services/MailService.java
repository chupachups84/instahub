package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vistar.practice.demo.dtos.mail.MailMessageDto;
import vistar.practice.demo.handler.exceptions.RevokedTokenException;
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
    @Value("${domain.address}")
    private String domain;
    @Value("${auth.uri}")
    private String uri;

    @Value("${email.token.expiration}")
    public Long expiration;

    @Value("${email.token.recover-expiration}")
    public Long recoverExpiration;

    public void saveConfirmationTokenMessage(UserEntity user, String token) {
        emailTokenRepository.save(
                EmailTokenEntity.builder()
                        .id(token)
                        .expiresAt(Instant.now().plus(Duration.ofMillis(expiration)))
                        .user(user).build()
        );
    }

    public void saveRecoverTokenMessage(UserEntity user, String token) {
        emailTokenRepository.save(
                EmailTokenEntity.builder()
                        .id(token)
                        .expiresAt(Instant.now().plus(Duration.ofMillis(recoverExpiration)))
                        .user(user).build()
        );
    }

    public void sendConfirmationTokenMessage(String email, String token) {
        kafkaSender.sendTransactionalMailMessage(
                topic,
                MailMessageDto.builder()
                        .email(email)
                        .subject("Confirmation message")
                        .message("Click on the link to confirm your email " + domain + "/" + uri + "/confirm?token=" + token)
                        .build()
        );
    }

    public void sendRecoverTokenMessage(String email, String token) {
        kafkaSender.sendTransactionalMailMessage(
                topic,
                MailMessageDto.builder()
                        .email(email)
                        .subject("Message for recover user")
                        .message("Click on the link to recover your account " + domain + "/" + uri + "/recover?token=" + token)
                        .build()
        );
    }

    public void revokeConfirmationTokenById(String token) {
        emailTokenRepository.findById(token)
                .filter(
                        eToken -> Instant.now().isBefore(eToken.getExpiresAt())
                )
                .filter(
                        eToken -> !eToken.isRevoked()
                ).ifPresentOrElse(
                        eToken -> eToken.setRevoked(true),
                        () -> {
                            throw new RevokedTokenException("invalid confirmation token");
                        }
                );
    }
    public void recoverUserByToken(String token) {
        emailTokenRepository.findById(token)
                .filter(
                        eToken -> Instant.now().isBefore(eToken.getExpiresAt())
                )
                .filter(
                        eToken -> !eToken.isRevoked()
                ).ifPresentOrElse(
                        eToken -> {
                            eToken.setRevoked(true);
                            eToken.getUser().setActive(true);
                        },
                        () -> {
                            throw new RevokedTokenException("invalid confirmation token");
                        }
                );
    }
}
