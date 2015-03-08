package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.properties.ContraptionProperties;
import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.utlity.Response;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/**
 * Represents a Contraption in the minecraft world
 */
public abstract class Contraption {

    Location location;
    ContraptionProperties properties;
    Set<BukkitTask> tasks;

    public Contraption(ContraptionProperties properties, Location location) {
        this.location = location;
        this.properties = properties;
    }
    
    public JSONObject save() {
        return properties.save(this);
    }
    
    public JSONObject getResources(){
        return new JSONObject();
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
    public Response trigger() {
        return new Response(true,"Contraption did nothing");
    }

    /*
     Updates the state of the contraption
     */
    public void update() {
        //Check energy, consume more if needed
        if (!isValid()) {
            getContraptionManager().destroy(this);
        }
    }
    
    public void update(Resource resource) {
        update();
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
        for (BukkitTask task : tasks) {
            try {
                task.cancel();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
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
    
    public boolean hasResource(String resourceID) {
        return false;
    }
    
    public Resource getResource(String resourceID) {
        return null;
    }
    
    public String getName() {
        return "No Name";
    }
}
