package me.gamenu.carbondf.exceptions;

public class InvalidFieldException extends CarbonRuntimeException{
    public InvalidFieldException() {
    }

    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFieldException(Throwable cause) {
        super(cause);
    }

    protected InvalidFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
