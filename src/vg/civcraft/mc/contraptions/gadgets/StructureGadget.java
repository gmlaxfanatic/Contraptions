package vg.civcraft.mc.contraptions.gadgets;


import java.util.Collection;
import org.bukkit.block.Block;
import vg.civcraft.mc.contraptions.contraptions.Contraption;
import vg.civcraft.mc.contraptions.utility.Anchor;
import vg.civcraft.mc.contraptions.utility.BlockLocation;
import vg.civcraft.mc.contraptions.utility.Offset;
import vg.civcraft.mc.contraptions.utility.Structure;

/**
 * Used to require that a contraption maintains a specific block configuration in
 * the world
 */
public class StructureGadget {

    Structure structure;
    //Offset of contraption within the structure
    Offset offset;
    
    public StructureGadget(Structure structure, Offset offset) {
        this.structure = structure;
        this.offset = offset;
    }
    
    public boolean exists(Anchor anchor) {
        return structure.exists(anchor);
    }
    
    public boolean validBlock(Block block) {
        return block.getState().getType().equals(structure.getMaterial(offset));
    }
    
    public Collection<BlockLocation> getBlockLocations(Anchor anchor) {
        return structure.getBlockLocations(anchor);
    }
    
    
}
