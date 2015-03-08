package com.untamedears.contraptions.utlity;

import org.bukkit.entity.Player;

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
    
    public void conveyTo(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
