package exception;


public class TextProcessingException extends RuntimeException {
    public TextProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
