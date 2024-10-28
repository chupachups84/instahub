package vistar.practice.demo.handler.exceptions;

public class NotUniqueEmailException extends RuntimeException {
    public NotUniqueEmailException() {
    }

    public NotUniqueEmailException(String s) {
        super(s);
    }
}
