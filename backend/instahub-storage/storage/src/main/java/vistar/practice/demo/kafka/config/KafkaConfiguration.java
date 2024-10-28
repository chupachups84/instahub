package vistar.practice.demo.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final ConsumerFactory<Object, Object> consumerFactory;

    @Value("${kafka.topic.dlq-prefix}")
    private String DLQ_PREFIX;

    /**
     * Фабрика листенера сообщений. В ней настраиваются характеристики, связанные с
     * листенерами Кафки.
     *
     * @param errorHandler Обработчик исключений
     * @return Бин
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setCommonErrorHandler(errorHandler);

        return kafkaListenerContainerFactory;
    }

    /**
     * Бин стандартного обработчика исключений. В нём прописывается логика обработки
     * исключений при обработке сообщения
     *
     * @param deadLetterPublishingRecoverer Бин, восстанавливающий сообщения
     * @return Бин
     */
    @Bean
    public DefaultErrorHandler defaultErrorHandler(
            DeadLetterPublishingRecoverer deadLetterPublishingRecoverer
    ) {
        final var handler = new DefaultErrorHandler(deadLetterPublishingRecoverer);
        handler.addNotRetryableExceptions(Exception.class);
        return handler;
    }

    /**
     * Бин, восстанавливающий сообщение, в результате обработки которого было выброшено
     * исключение. Именно он перенаправляет сообщение в отработчик dead letter
     *
     * @param bytesTemplate Экземпляр KafkaTemplate
     * @return Бин
     */
    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaTemplate<Object, Object> bytesTemplate
    ) {
        return new DeadLetterPublishingRecoverer(bytesTemplate, ((consumerRecord, ex) ->
                new TopicPartition(DLQ_PREFIX + consumerRecord.topic(), consumerRecord.partition())));
    }
}
