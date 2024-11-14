package me.gamenu.carbondf.exceptions;

public class CarbonRuntimeException extends RuntimeException {

    public CarbonRuntimeException() {
    }

    public CarbonRuntimeException(String message) {
        super(message);
    }

    public CarbonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarbonRuntimeException(Throwable cause) {
        super(cause);
    }

    protected CarbonRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
