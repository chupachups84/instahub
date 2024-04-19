package vistar.practice.demo.handler;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vistar.practice.demo.dtos.response.ExceptionResponse;
import vistar.practice.demo.dtos.response.ValidationExceptionResponse;
import vistar.practice.demo.handler.exceptions.InvalidAccountException;
import vistar.practice.demo.handler.exceptions.RevokedTokenException;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                "Incorrect password",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
  /*  @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                HttpStatus.BAD_REQUEST,
                exception.getAllErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList(),
                "input data is not valid"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }*/
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleException(DataIntegrityViolationException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "this request leads to violate data structure",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(EntityNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                "required data is not found",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UsernameNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                "this username is not found",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidAccountException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.GONE,
                "this username is deleted by himself",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.GONE);
    }

    @ExceptionHandler(RevokedTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(RevokedTokenException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                "this token is revoked",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionResponse> handleException(MalformedJwtException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                "token is not correctly constructed",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleException(ExpiredJwtException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                "token is expired",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleException(SignatureException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                "problem with digital signature",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidKeyException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                "token was edit manually",
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
