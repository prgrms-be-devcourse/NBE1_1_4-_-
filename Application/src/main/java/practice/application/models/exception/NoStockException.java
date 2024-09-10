package practice.application.models.exception;

public class NoStockException extends RuntimeException{

    public NoStockException() {
    }

    public NoStockException(String message) {
        super(message);
    }

    public NoStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoStockException(Throwable cause) {
        super(cause);
    }
}
