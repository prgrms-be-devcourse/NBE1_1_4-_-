package practice.application.models.exception;

public class ImpossibleCancelException extends RuntimeException{
    public ImpossibleCancelException() {
    }

    public ImpossibleCancelException(String message) {
        super(message);
    }

    public ImpossibleCancelException(Throwable cause) {
        super(cause);
    }

    public ImpossibleCancelException(String message, Throwable cause) {
        super(message, cause);
    }
}
