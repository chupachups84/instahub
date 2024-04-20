package vistar.practice.demo.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidationExceptionResponse {
    HttpStatus status;
    List<String> errorsMessage;
    String message;
}
