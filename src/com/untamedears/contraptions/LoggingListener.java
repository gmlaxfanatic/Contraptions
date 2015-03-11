package com.untamedears.contraptions;

import com.untamedears.contraptions.events.ContraptionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
/**
 * This listens for ContraptionEvents and then reports their messages to the console.
 * @author gmlaxfanatic2@gmail.com
 */
public class LoggingListener implements Listener {
    
    ContraptionManager contraptionManager;

    /**
     * Creates the Logging listener
     */
    public LoggingListener() {
    }

    /**
     * Called when an important event happens in Contraptions
     *
     * @param e A ContraptionEvent
     */
    @EventHandler
    public void onContraptionEvent(ContraptionEvent e) {
        ContraptionsPlugin.toConsole(e.getMessage());
    }
}
