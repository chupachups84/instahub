package vistar.practice.demo.handler.exceptions;

public class InvalidConfirmationToken extends RuntimeException {
    public InvalidConfirmationToken() {
    }

    public InvalidConfirmationToken(String s) {
        super(s);
    }
}
