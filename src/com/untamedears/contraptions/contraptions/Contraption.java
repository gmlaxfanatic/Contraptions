package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.properties.ContraptionProperties;
import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.utility.Response;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/**
 * Represents a specific contraption in the minecraft world
 * <p>
 * A contraption is a single functionalized block in the minecraft world. How it
 * behaves is dictated by its code, and the gadgets contained within its
 * properties file. Its current state is defined by its resources, which are
 * simply wrapped integer values. Each implementation of a contraption should
 * have an associated implementation of a ContraptionProperties file, which
 * contains the shared information on that Contraption's implementation,
 * primarily its assorted gadgets.
 * <p>
 * A Contraption's code will piece together current gadgets, each of which have
 * specific functions, to form a coherent Contraption. The Contraptions state is
 * stored in its resources, access to which can be given to multiple gadgets,
 * allowing the gadgets to naturally influence each other. The Contraptions code
 * can also access its resources to create more tailored interactions between
 * gadgets, however complex and encapsulated functionality should be encoded as
 * additional gadgets for a Contraption to use, allowing greater flexibility for
 * future reuse by other Contraption implementations.
 * <p>
 * Some gadgets will also generate tasks specific to a contraption which run
 * over time. These tasks are also stored within the contraption to allow
 * cancellation upon Contraption destruction.
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

    public JSONObject getResources() {
        return new JSONObject();
    }

    public void loadResources(JSONObject jsonObject) {
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
        return new Response(true, "Contraption did nothing");
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
