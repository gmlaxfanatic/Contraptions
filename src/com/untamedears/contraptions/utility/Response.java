package com.untamedears.contraptions.utility;

import org.bukkit.entity.Player;

/**
 * Conveys a response to a triggered event
 */
public class Response {

    public final boolean success;
    public final String message;

    /**
     * Creates the response
     * @param success Whether the action was successful
     * @param message A message associated with the action
     */
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    /**
     * Messages the player about the response
     * @param player The player to massage
     */
    public void conveyTo(Player player) {
        player.sendMessage(message);
    }

}
