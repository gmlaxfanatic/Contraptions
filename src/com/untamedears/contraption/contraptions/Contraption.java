package com.untamedears.contraption.contraptions;

import com.untamedears.properties.ContraptionProperties;
import com.untamedears.contraption.ContraptionManager;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a Contraption in the minecraft world
 */
public class Contraption {

    Location location;
    ContraptionProperties properties;
    
    public Contraption(ContraptionProperties properties, Location location) {
        this.location = location;
        this.properties = properties;
    }
    
    protected ContraptionProperties getProperties() {
        return properties;
    }

    public Location getLocation() {
        return location;
    }

    /*
     * Triggers the block to perform a generic action
     * returns whether the triggering was successful
     */
    public boolean trigger() {
        return true;
    }

    /*
    Updates the state of the contraption
    */
    public void update() {
        //Check energy, consume more if needed
        if(!isValid()) {
            getContraptionManager().destroy(this);
        }
    }
    
    /*
     * Checks if the material for this block is at the location
     */
    public boolean isValid() {
        return location.getBlock().getType().equals(properties.getMaterial());
    }

    /*
     * Inactivates associated modules
     */
    public void destroy() {
    }

    /*
     *
     */
    public Inventory getInventory() {
        if (location.getBlock().getState() instanceof InventoryHolder) {
            return ((InventoryHolder) location.getBlock().getState()).getInventory();
        }
        return null;
    }

    public ContraptionManager getContraptionManager() {
        return properties.getContraptionManager();
    }
}
