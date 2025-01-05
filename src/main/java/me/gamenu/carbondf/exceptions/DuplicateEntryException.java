package me.gamenu.carbondf.exceptions;

public class DuplicateEntryException extends CarbonRuntimeException{

    public DuplicateEntryException() {
    }

    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEntryException(Throwable cause) {
        super(cause);
    }
}
