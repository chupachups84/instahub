package vistar.practice.demo.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.mail.MailMessageDto;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;

@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * Отправить транзакционное сообщение в указанный топик
     *
     * @param topic Топик для отправки
     * @param photoStorageDto Отправляемое сообщение
     */
    @Transactional("kafkaTransactionManager")
    public void sendTransactionalMessage(String topic, PhotoStorageDto photoStorageDto) {
        kafkaTemplate.send(topic, String.valueOf(photoStorageDto.getOwnerId()), photoStorageDto);
    }

    /**
     * Отправить нетранзакционное сообщение в указанный топик
     *
     * @param topic Топик для отправки
     * @param photoStorageDto Отправляемое сообщение
     */
    public void sendNonTransactionalMessage(String topic, PhotoStorageDto photoStorageDto) {
        kafkaTemplate.getProducerFactory().createNonTransactionalProducer().send(
                new ProducerRecord<>(topic, String.valueOf(photoStorageDto.getOwnerId()), photoStorageDto)
        );
    }

    /**
     * Отправить транзакционное сообщение в указанный топик
     *
     * @param topic Топик для отправки
     * @param mailMessageDto Отправляемое сообщение
     */
    @Transactional("kafkaTransactionManager")
    public void sendTransactionalMailMessage(String topic, MailMessageDto mailMessageDto) {
        kafkaTemplate.send(topic, mailMessageDto);
    }
}
