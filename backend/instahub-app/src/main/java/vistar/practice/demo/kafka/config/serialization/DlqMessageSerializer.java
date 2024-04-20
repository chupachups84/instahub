package vistar.practice.demo.kafka.config.serialization;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DlqMessageSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException ex) {
            throw new SerializationException("Serializing error. JSON was not received", ex);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, T data) {
        return serialize(topic, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
