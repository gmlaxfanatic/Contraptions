package com.untamedears.contraptions.utility;

/**
 * The ContraptionException is thrown by the contraption classes when things are
 * amiss.
 */
public class ContraptionException extends Exception {

    private static final long serialVersionUID = 0;
    private Throwable cause;

    /**
     * Constructs a ContraptionException with an explanatory message.
     * 
     * @param message Detail about the reason for the exception.
     */
    public ContraptionException(String message) {
        super(message);
    }

    /**
     * Constructs a new ContraptionException with the specified cause.
     * 
     * @param cause The cause.
     */
    public ContraptionException(Throwable cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    /**
     * Returns the cause of this exception or null if the cause is nonexistent
     * or unknown.
     * 
     * @return the cause of this exception or null if the cause is nonexistent
     *         or unknown.
     */
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
