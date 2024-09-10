package practice.application.models.exception;

public class OrderAlreadyCancelledException extends RuntimeException{
    public OrderAlreadyCancelledException() {
    }

    public OrderAlreadyCancelledException(String message) {
        super(message);
    }

    public OrderAlreadyCancelledException(Throwable cause) {
        super(cause);
    }

    public OrderAlreadyCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
}
