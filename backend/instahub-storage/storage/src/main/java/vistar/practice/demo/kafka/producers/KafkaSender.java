package vistar.practice.demo.kafka.producers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;

@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * Отправить транзакционное сообщение в указанный топик
     *
     * @param topic Топик для отправки
     * @param photoInfoDto Отправляемое сообщение
     */
    @Transactional("kafkaTransactionManager")
    public void sendTransactionalMessage(String topic, PhotoInfoDto photoInfoDto) {
        kafkaTemplate.send(topic, String.valueOf(photoInfoDto.getOwnerId()), photoInfoDto);
    }

    /**
     * Отправить нетранзакционное сообщение в указанный топик
     *
     * @param topic Топик для отправки
     * @param photoInfoDto Отправляемое сообщение
     */
    public void sendNonTransactionalMessage(String topic, PhotoInfoDto photoInfoDto) {
        kafkaTemplate.getProducerFactory().createNonTransactionalProducer().send(
                new ProducerRecord<>(topic, String.valueOf(photoInfoDto.getOwnerId()), photoInfoDto)
        );
    }
}
