package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.ContraptionsPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Response;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Specifies the properties of a Contraption's implementation
 * 
 * A Contraption's properties define how all Contraptions of that type behave.
 * Each Contraption implementation will have a accompanying properties
 * implementation which specifies for the Contraption its material, ID, common
 * name, and collection of widgets. This differs from the Contraption's
 * resources which are specific variables held by a contraption object dictating
 * its current state.
 */
public abstract class ContraptionProperties {

    ContraptionManager contraptionManager;
    Material material;
    String ID;
    String name;

    /**
     * Creates a new instance of Contraption properties
     * 
     * @param contraptionManager The ContraptionManager object
     * @param ID                 A unique string associated with this
     *                           Contraption specification
     * @param material           The material these contraptions are made from
     */
    public ContraptionProperties(ContraptionManager contraptionManager, String ID, Material material) {
        this.contraptionManager = contraptionManager;
        this.ID = ID;
        this.material = material;

    }

    /**
     * Saves A contraption to a JSONObject
     *
     * @param contraption Contraption to save
     * @return JSONObject representing the contraption
     */
    public JSONObject save(Contraption contraption) {
        JSONObject saveJSON = new JSONObject();
        saveJSON.put("Type", getType());
        saveJSON.put("ID", getID());
        ContraptionsPlugin.toConsole(contraption.getLocation().toString());
        
        ContraptionsPlugin.toConsole(contraption.getLocation().getWorld().toString());
        String location = "[" + contraption.getLocation().getWorld().getUID() + ","
                + contraption.getLocation().getBlockX() + ","
                + contraption.getLocation().getBlockY() + ","
                + contraption.getLocation().getBlockZ() + "]";
        saveJSON.put("Location", new JSONArray(location));
        saveJSON.put("Resources", contraption.getResources());
        return saveJSON;
    }

    /**
     * Safely creates a contraption at the given location
     *
     * @param location The Location to create the Contraption
     * @return A Response to the success of the creation
     */
    public abstract Response createContraption(Location location);

    /**
     * Gets the type of the properties
     *
     * @return The ID for this Properties class
     */
    public abstract String getType();

    /**
     * Generates a new Contraption object at the given location
     *
     * @param location Location to generate Contraption
     * @return The created Contraption
     */
    public abstract Contraption newContraption(Location location);

    /**
     * Loads a Contraption from a file
     *
     * @param jsonObject JSONObject representing the Contraption
     * @return The loaded Contraption
     */
    public Contraption loadContraption(JSONObject jsonObject) {
        ContraptionsPlugin.toConsole(jsonObject.toString());
        JSONArray locationArray = jsonObject.getJSONArray("Location");
        Location location = new Location(ContraptionsPlugin.getContraptionPlugin().getServer().getWorld(UUID.fromString(locationArray.getString(0))),
                locationArray.getInt(1), locationArray.getInt(2), locationArray.getInt(3));
        Contraption contraption = newContraption(location);
        contraption.loadResources(jsonObject.getJSONObject("Resources"));
        return contraption;
    }

    /**
     * Checks if this is a valid block for this kind of Contraption
     *
     * @param block Block to check
     * @return If Block was valid
     */
    public boolean validBlock(Block block) {
        return block.getState().getType().equals(material);
    }

    /**
     * Gets the ContraptionManager
     *
     * @return The ContraptionManager
     */
    public ContraptionManager getContraptionManager() {
        return contraptionManager;
    }

    /**
     * Gets the ID associated with the set of values for a Properties file
     *
     * @return The ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Gets the material used for this kind of Contraption
     *
     * @return The Material of the Contraption
     */
    public Material getMaterial() {
        return material;
    }
}
