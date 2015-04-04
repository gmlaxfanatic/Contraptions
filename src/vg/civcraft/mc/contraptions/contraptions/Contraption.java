package vg.civcraft.mc.contraptions.contraptions;

import java.util.Collection;
import vg.civcraft.mc.contraptions.properties.ContraptionProperties;
import vg.civcraft.mc.contraptions.ContraptionManager;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;
import vg.civcraft.mc.contraptions.utility.Resource;
import vg.civcraft.mc.contraptions.utility.Response;
import vg.civcraft.mc.contraptions.utility.SoundType;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;
import vg.civcraft.mc.citadel.ReinforcementManager;
import vg.civcraft.mc.citadel.reinforcement.PlayerReinforcement;
import vg.civcraft.mc.contraptions.utility.Anchor;
import vg.civcraft.mc.contraptions.utility.BlockLocation;
import vg.civcraft.mc.namelayer.group.Group;

/**
 * Represents a specific contraption in the minecraft world
 *
 * A contraption is a single functionalized block in the minecraft world. How it
 * behaves is dictated by its code, and the gadgets contained within its
 * properties file. Its current state is defined by its resources, which are
 * simply wrapped integer values. Each implementation of a contraption should
 * have an associated implementation of a ContraptionProperties file, which
 * contains the shared information on that Contraption's implementation,
 * primarily its assorted gadgets.
 *
 * A Contraption's code will piece together current gadgets, each of which have
 * specific functions, to form a coherent Contraption. The Contraptions state is
 * stored in its resources, access to which can be given to multiple gadgets,
 * allowing the gadgets to naturally influence each other. The Contraptions code
 * can also access its resources to create more tailored interactions between
 * gadgets, however complex and encapsulated functionality should be encoded as
 * additional gadgets for a Contraption to use, allowing greater flexibility for
 * future reuse by other Contraption implementations.
 *
 * Some gadgets will also generate tasks specific to a contraption which run
 * over time. These tasks are also stored within the contraption to allow
 * cancellation upon Contraption destruction.
 */
public abstract class Contraption {

    Anchor anchor;

    ContraptionProperties properties;
    Set<BukkitTask> tasks;

    /**
     * Creates a contraption
     *
     * @param properties Properties associated with the Contraption
     * @param anchor   Anchor of the Contraption
     */
    public Contraption(ContraptionProperties properties, Anchor anchor) {
        this.anchor = anchor;
        this.properties = properties;
        tasks = new HashSet<BukkitTask>();
    }

    /**
     * Saves the Contraption to a JSONObject
     *
     * @return JSONObject of the contraption
     */
    public JSONObject save() {
        return properties.save(this);
    }

    /**
     * Gets a JSONObject representing the resources contained in the Contraption
     *
     * @return The JSONObject representing the resources contained in the
     *         Contraption
     */
    public JSONObject getResources() {
        return new JSONObject();
    }

    /**
     * Loads the resources from a JSONObject
     *
     * @param jsonObject JSONObject containing resource information
     */
    public void loadResources(JSONObject jsonObject) {
    }

    /**
     * Gets the Properties object used by this Contraption
     *
     * @return The Properties object used by this Contraption
     */
    protected ContraptionProperties getProperties() {
        return properties;
    }

    /**
     * Gets the Location of the Contraption
     *
     * @return The Location of the Contraption
     */
    public BlockLocation getLocation() {
        return anchor.getLocationOfOffset(getProperties().getStructureGadget().getOffset());
    }
    /**
     * Gets the Anchor of the Contraption
     * @return Anchor of the Contraption
     */
    public Anchor getAnchor() {
       return anchor; 
    }

    /**
     * Triggers the block to performs its action
     *
     * @return A Response describing the result of the triggering
     */
    public Response trigger() {
        return new Response(true, "Contraption did nothing");
    }

    /**
     * Updates the state of the contraption
     */
    public void update() {
        //Check energy, consume more if needed
        if (!isValid()) {
            getContraptionManager().destroy(this);
        }
    }

    /**
     * Updates the state of a contraption given that a resource has changed
     *
     * @param resource The changed resource
     */
    public void update(Resource resource) {
        update();
    }

    /**
     * Checks if the material for this block is at the location
     *
     * @return If the Material is correct
     */
    public boolean isValid() {
        return properties.getStructureGadget().exists(anchor);
    }

    /**
     * Inactivates associated modules for the Contraption
     */
    public void destroy() {
        for (BukkitTask task : tasks) {
            try {
                task.cancel();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
        SoundType.DESTRUCTION.play(anchor.getLocation());
    }

    /**
     * Gets the inventory of the contraption
     *
     * @return The inventory of the contraption
     */
    public Inventory getInventory() {
        if (anchor.getLocation().getBukkitLocation().getBlock().getState() instanceof InventoryHolder) {
            return ((InventoryHolder) anchor.getLocation().getBukkitLocation().getBlock().getState()).getInventory();
        }
        return null;
    }

    /**
     * Gets the ContraptionManager
     *
     * @return the ContraptionManager
     */
    public ContraptionManager getContraptionManager() {
        return properties.getContraptionManager();
    }

    /**
     * Checks if the Contraptions has a resource of a particular ID
     *
     * @param resourceID The Resource ID, defined by the Contraption
     *                   implementation
     * @return Check if the Contraption has a resource of resourceID
     */
    public boolean hasResource(String resourceID) {
        return false;
    }

    /**
     * Gets a specific resource from the Contraption
     *
     * @param resourceID The ID of the resource, as defined by the Contraption
     *                   implmentation
     * @return The Resource
     */
    public Resource getResource(String resourceID) {
        return null;
    }

    /**
     * Gets the readible name of the Contraption
     *
     * @return The Contraption Name
     */
    public String getName() {
        return "No Name";
    }

    /**
     * Gets the group associated with the reinforcement of this Contraptions
     *
     * @return The NameLayer Group for this Contraptions
     */
    public Group getGroup() {
        ReinforcementManager reinforcementManager = getContraptionManager().getReinforcementManager();
        if (ContraptionsPlugin.PERMISSIONS && reinforcementManager.isReinforced(anchor.getLocation().getBukkitLocation())) {
            return ((PlayerReinforcement) reinforcementManager.getReinforcement(anchor.getLocation().getBukkitLocation())).getGroup();
        }
        return null;
    }
    
    public Collection<BlockLocation> getBlockLocations() {
        return properties.getStructureGadget().getBlockLocations(anchor);
    }
    
    public void blockDestroyed(Block block) {
        
    }
}
