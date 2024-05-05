package vistar.practice.demo.configs.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.io.InputStreamSource;

import java.io.IOException;

@JsonComponent
public class StreamSerializer extends JsonSerializer<InputStreamSource> {
    @Override
    public void serialize(
            InputStreamSource inputStreamSource,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeBinary(inputStreamSource.getInputStream(), -1);
    }
}
