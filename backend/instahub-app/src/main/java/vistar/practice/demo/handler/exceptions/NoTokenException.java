package vistar.practice.demo.handler.exceptions;

public class NoTokenException extends RuntimeException {
    public NoTokenException() {
    }

    public NoTokenException(String s) {
        super(s);
    }
}
