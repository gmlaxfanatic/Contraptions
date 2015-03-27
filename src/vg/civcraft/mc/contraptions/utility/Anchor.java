package vg.civcraft.mc.contraptions.utility;

import java.io.Serializable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.json.JSONObject;

/**
 * Represents a location with a orientation
 */
public class Anchor implements Serializable {

    //Describes the oritentation of the structure

    public enum Orientation {

        NE(0),
        SE(1),
        SW(2),
        NW(3);

        final int id;
        static final Orientation[] byId = {Orientation.NE, Orientation.SE, Orientation.SW, Orientation.NW};

        private Orientation(int id) {
            this.id = id;
        }

        static Orientation getOrientation(int id) {
            return id < byId.length ? byId[id] : null;
        }
    }

    public final Orientation orientation;
    public final BlockLocation location;

    public Anchor(Orientation orientation, Location location) {

        this.orientation = orientation;
        this.location = new BlockLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    /**
     * Gets either a negative or positive increment of X depending on its
     * orientation
     * 
     * @return A direction of this anchor 
     */
    public int getXModifier() {
        return orientation == Orientation.NE || orientation == Orientation.NW ? 1 : -1;
    }

    /**
     * Gets either a negative or positive increment of X depending on its
     * orientation
     * 
     * @return Z direction of this anchor
     */
    public int getZModifier() {
        return orientation == Orientation.NE || orientation == Orientation.SE ? 1 : -1;
    }

    /**
     * Returns a location the offset given this anchor
     * 
     * @param offset An offset from the anchor
     * @return Location representing the location of the Offset
     */
    public BlockLocation getLocationOfOffset(Offset offset) {
        Offset orientedOffset = offset.orient(orientation);
        return location.add(orientedOffset.x, orientedOffset.y, orientedOffset.z);
    }

    /**
     * Gets the block located in the world given the offset and this anchor
     * 
     * @return The Block at the given offset
     */
    public Block getBlock(Offset offset) {
        return getLocationOfOffset(offset).getBlock();
    }

    /**
     * Check if location is contained within a bounding box at this anchor
     * TODO: This looks wrong
     * @return If the location is contained within the bounding box
     */
    public boolean containedIn(Location testLocation, int[] dimensions) {
        if ((testLocation.getBlockX() - location.x) < dimensions[0] * getXModifier()) {
            if ((testLocation.getBlockZ() - location.z) < dimensions[2] * getZModifier()) {
                if (0 <= (testLocation.getBlockZ() - location.z) && (testLocation.getBlockZ() - location.z) < dimensions[1]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Location getBukkitLocation() {
        return location.getLocation();
    }
    
    public static Anchor fromJSON(JSONObject jsonObject) {
        throw new UnsupportedOperationException("Method not supported yet");
    }
}
