package by.motolyha.multithreading.exception;

public class ThreadException extends Exception {
    public ThreadException() {
        super();
    }

    public ThreadException(String message) {
        super(message);
    }

    public ThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadException(Throwable cause) {
        super(cause);
    }
}