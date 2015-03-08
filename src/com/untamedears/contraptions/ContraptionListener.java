package com.untamedears.contraptions;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listens for events for the Contraptions Plugin
 */
public class ContraptionListener implements Listener {

    ContraptionManager contraptionManager;

    /**
     * Creates the listener
     *
     * @param contraptionManager The ContraptionManager which deals with events
     */
    public ContraptionListener(ContraptionManager contraptionManager) {
        this.contraptionManager = contraptionManager;
    }

    /**
     * Called when a player left or right clicks. Triggers functional blocks if
     * they exist at that location
     *
     * @param e A PlayerInteractEvent
     */
    @EventHandler
    public void playerInteractionEvent(PlayerInteractEvent e) {
        contraptionManager.handleInteraction(e);
    }

    /**
     * Called when a block is broken
     *
     * @param e The BlockBreakEvent
     */
    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        contraptionManager.handleBlockDestruction(e.getBlock());
    }

    /**
     * Called when a entity explodes(creeper,tnt etc.)
     *
     * @param e The EntityExplodeEvent
     */
    @EventHandler
    public void explosionListener(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            contraptionManager.handleBlockDestruction(block);
        }
    }

    /**
     * Called when a block burns
     *
     * @param e The BlockBurnEvent
     */
    @EventHandler
    public void burnListener(BlockBurnEvent e) {
        contraptionManager.handleBlockDestruction(e.getBlock());
    }
}
