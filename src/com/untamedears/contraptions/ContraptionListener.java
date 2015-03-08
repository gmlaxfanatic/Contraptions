package com.untamedears.contraptions;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ContraptionListener implements Listener {

    ContraptionManager contraptionManager;

    public ContraptionListener(ContraptionManager contraptionManager) {
        this.contraptionManager = contraptionManager;
    }

    /**
     * Called when a player left or right clicks. Triggers functional blocks if
     * they exist at that location
     */
    @EventHandler
    public void playerInteractionEvent(PlayerInteractEvent e) {
        contraptionManager.handleInteraction(e);
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        contraptionManager.handleBlockDestruction(e.getBlock());
    }

    /**
     * Called when a entity explodes(creeper,tnt etc.)
     */
    @EventHandler
    public void explosionListener(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            contraptionManager.handleBlockDestruction(block);
        }
    }

    /**
     * Called when a block burns
     */
    @EventHandler
    public void burnListener(BlockBurnEvent e) {
        contraptionManager.handleBlockDestruction(e.getBlock());
    }
}
