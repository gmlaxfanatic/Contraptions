package vg.civcraft.mc.contraptions.gadgets;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import org.bukkit.block.Block;
import org.json.JSONObject;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;
import vg.civcraft.mc.contraptions.utility.Anchor;
import vg.civcraft.mc.contraptions.utility.BlockLocation;
import vg.civcraft.mc.contraptions.utility.JSONHelpers;
import vg.civcraft.mc.contraptions.utility.Offset;
import vg.civcraft.mc.contraptions.utility.Structure;

/**
 * Used to require that a contraption maintains a specific block configuration
 * in the world
 */
public class StructureGadget {

    Structure structure;
    //Offset of contraption within the structure
    Offset offset;

    /**
     * Creates a Structure Gadget
     *
     * @param structure The Structure
     * @param offset The Offset of the Contraption within the Stucture
     */
    public StructureGadget(Structure structure, Offset offset) {
        this.structure = structure;
        this.offset = offset;
    }

    /**
     * Checks if Structure exists given an anchor
     *
     * @param anchor The anchor of the structure in the world
     * @return If the Structure exists at the Anchor
     */
    public boolean exists(Anchor anchor) {
        return structure.exists(anchor);
    }

    /**
     * Checks if a structure exists given the location of the Contraption
     *
     * @param location Location of the Contraption
     * @return The Anchor of the structure if it exists, null otherwise
     */
    public Anchor exists(BlockLocation location) {
        Set<Anchor> anchors = offset.getPotentialAnchors(location);
        for (Anchor anchor : anchors) {
            if (exists(anchor)) {
                return anchor;
            }
        }
        return null;
    }

    /**
     * Checks if a block is a valid block for a Contraptpion with this Structure
     *
     * @param block Block to check
     * @return If block matches the Contraption's Material
     */
    public boolean validBlock(Block block) {
        ContraptionsPlugin.toConsole(structure.getMaterial(offset).toString());
        ContraptionsPlugin.toConsole(block.getType().toString());
        return block.getState().getType().equals(structure.getMaterial(offset));
    }

    /**
     * Gets all locations associated with this structure and a given anchor
     *
     * @param anchor Anchor of Structure
     * @return All location associated with the structure
     */
    public Collection<BlockLocation> getBlockLocations(Anchor anchor) {
        return structure.getBlockLocations(anchor);
    }

    public static StructureGadget fromJSON(JSONObject jsonObject) {
        String filename = JSONHelpers.loadString(jsonObject, "structure_file");
        File file = new File(filename);
        Offset offset = Offset.fromJSON(jsonObject);
        Structure structure =  Structure.parseSchematic(file);
        return new StructureGadget(structure,offset);
    }
}
