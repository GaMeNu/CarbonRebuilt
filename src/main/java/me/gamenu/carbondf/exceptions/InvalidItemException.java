package me.gamenu.carbondf.exceptions;

/**
 * This should be used if the user used an item that isn't allowed in the context.<br/>
 * Unlike {@link TypeException}, this is used to prevent the user from, for example, using block tags as items.
 * {@link TypeException} should instead be used for Type checking and analysis
 */
public class InvalidItemException extends CarbonRuntimeException{
    public InvalidItemException() {
        super();
    }

    public InvalidItemException(String message) {
        super(message);
    }

    public InvalidItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidItemException(Throwable cause) {
        super(cause);
    }
}
