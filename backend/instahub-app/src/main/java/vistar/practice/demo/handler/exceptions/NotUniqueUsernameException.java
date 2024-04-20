package vistar.practice.demo.handler.exceptions;

public class NotUniqueUsernameException extends RuntimeException {
    public NotUniqueUsernameException() {
    }

    public NotUniqueUsernameException(String s) {
        super(s);
    }
}
