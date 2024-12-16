package me.gamenu.carbondf.exceptions;

public class ArgsOverflowException extends CarbonRuntimeException {
    public ArgsOverflowException() {
    }

    public ArgsOverflowException(String message) {
        super(message);
    }

    public ArgsOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgsOverflowException(Throwable cause) {
        super(cause);
    }
}
