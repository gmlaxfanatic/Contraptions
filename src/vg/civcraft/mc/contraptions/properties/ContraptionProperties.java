package vg.civcraft.mc.contraptions.properties;

import vg.civcraft.mc.contraptions.ContraptionManager;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;
import vg.civcraft.mc.contraptions.contraptions.Contraption;
import vg.civcraft.mc.contraptions.utility.Response;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.json.JSONArray;
import org.json.JSONObject;
import vg.civcraft.mc.contraptions.gadgets.StructureGadget;
import vg.civcraft.mc.contraptions.utility.Anchor;
import vg.civcraft.mc.contraptions.utility.BlockLocation;
import vg.civcraft.mc.contraptions.utility.Offset;
import vg.civcraft.mc.contraptions.utility.Structure;

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
    StructureGadget structureGadget;
    String ID;
    String name;
    StructureGadget SG_DEFAULT = new StructureGadget(new Structure(new byte[][][]{{{(byte) Material.CHEST.getId()}}}), new Offset(0, 0, 0));

    /**
     * Creates a new instance of Contraption properties
     *
     * @param contraptionManager The ContraptionManager object
     * @param ID A unique string associated with this Contraption specification
     * @param name The Name this Contraption to referred to by
     */
    public ContraptionProperties(ContraptionManager contraptionManager, String ID, String name) {
        this.contraptionManager = contraptionManager;
        this.ID = ID;
        this.name = name;
        this.structureGadget = SG_DEFAULT;
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
                + contraption.getLocation().x + ","
                + contraption.getLocation().y + ","
                + contraption.getLocation().z + "]";
        saveJSON.put("Location", new JSONArray(location));
        saveJSON.put("Resources", contraption.getResources());
        return saveJSON;
    }

    /**
     * Safely creates a contraption at the given location
     *
     * @param location Location to create Contraption
     * @return A Response to the success of the creation
     */
    public abstract Response createContraption(BlockLocation location);

    /**
     * Gets the type of the properties
     *
     * @return The ID for this Properties class
     */
    public abstract String getType();

    /**
     * Generates a new Contraption object at the given Anchor
     *
     * @param anchor Anchor to generate Contraption
     * @return The created Contraption
     */
    public abstract Contraption newContraption(Anchor anchor);

    /**
     * Loads a Contraption from a file
     *
     * @param jsonObject JSONObject representing the Contraption
     * @return The loaded Contraption
     */
    public Contraption loadContraption(JSONObject jsonObject) {
        ContraptionsPlugin.toConsole(jsonObject.toString());
        Contraption contraption = newContraption(Anchor.fromJSON(jsonObject.getJSONObject("anchor")));
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
        return structureGadget.validBlock(block);
    }

    /**
     * Gets the StructureGadget
     *
     * @return The StructureGadget
     */
    public StructureGadget getStructureGadget() {
        return structureGadget;
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
     * Gets the name of the Contraption
     *
     * @return The Name of the Contraption
     */
    public String getName() {
        return name;
    }
}
