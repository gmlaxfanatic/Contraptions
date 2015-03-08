package com.untamedears.contraptions.utlity;

/**
 * Conveys a response to a triggered event
 */
public class Response {

    public final boolean success;
    public final String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
