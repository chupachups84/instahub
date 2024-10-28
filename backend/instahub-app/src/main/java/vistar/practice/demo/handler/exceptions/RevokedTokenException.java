package vistar.practice.demo.handler.exceptions;

import io.jsonwebtoken.JwtException;

public class RevokedTokenException extends JwtException {
    public RevokedTokenException(String message) {
        super(message);
    }
    public RevokedTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
