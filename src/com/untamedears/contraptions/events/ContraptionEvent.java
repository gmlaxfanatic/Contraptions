package com.untamedears.contraptions.events;

import com.untamedears.contraptions.contraptions.Contraption;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event for when important things happen to contraptions
 *
 */
public class ContraptionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    String message;
    Contraption contraption;
    Player player;

    /**
     * Creates a ContraptionEvent
     *
     * @param message     Text about the event
     * @param contraption The associated Contraption
     */
    public ContraptionEvent(String message, Contraption contraption) {
        this.message = message;
        this.contraption = contraption;
    }

    /**
     * Creates a ContraptionEvent
     *
     * @param message     Text about the event
     * @param contraption The associated Contraption
     * @param player      The associated Player
     */
    public ContraptionEvent(String message, Contraption contraption, Player player) {
        this(message, contraption);
        this.player = player;
    }

    /**
     * Gets the messsage
     *
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the associated Contraption
     *
     * @return The associated Contraption
     */
    public Contraption getContraption() {
        return contraption;
    }

    /**
     * Gets the associated Player
     *
     * @return The associated Plater
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
