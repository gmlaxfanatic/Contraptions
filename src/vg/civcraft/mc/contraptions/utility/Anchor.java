package vg.civcraft.mc.contraptions.utility;

import java.io.Serializable;
import org.bukkit.block.Block;
import org.json.JSONObject;

/**
 * Represents a location with a orientation
 */
public class Anchor implements Serializable {

    /**
     * Describes the oritentation of the structure
     */
    public enum Orientation {

        /**
         * NorthEast
         */
        NE(0),
        /**
         * SouthEast
         */
        SE(1),
        /**
         * SouthWest
         */
        SW(2),
        /**
         * NorthWest
         */
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

    /**
     * Orientation of the Anchor
     */
    public final Orientation orientation;

    /**
     * Location of the Anchor
     */
    public final BlockLocation location;

    /**
     * Creates an Anchor
     *
     * @param orientation Orientation of the Anchor
     * @param location Location of the Anchor
     */
    public Anchor(Orientation orientation, BlockLocation location) {

        this.orientation = orientation;
        this.location = new BlockLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    /**
     * Gets either a negative or positive increment of X depending on its
     * orientation
     *
     * @return An X direction of this anchor
     */
    public int getXModifier() {
        return orientation == Orientation.NE || orientation == Orientation.NW ? 1 : -1;
    }

    /**
     * Gets either a negative or positive increment of Z depending on its
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
     * @param offset
     * @return The Block at the given offset
     */
    public Block getBlock(Offset offset) {
        return getLocationOfOffset(offset).getBlock();
    }

    /**
     * Check if location is contained within a bounding box at this anchor TODO:
     * This looks wrong
     *
     * @param testLocation Location to test
     * @param dimensions An array of the form int[]{xDim,yDim,zDim}
     * @return If the location is contained within the bounding box
     */
    public boolean containedIn(BlockLocation testLocation, int[] dimensions) {
        if ((testLocation.getX() - location.x) < dimensions[0] * getXModifier()) {
            if ((testLocation.getZ() - location.z) < dimensions[2] * getZModifier()) {
                if (0 <= (testLocation.getY() - location.y) && (testLocation.getY() - location.y) < dimensions[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Imports this Anchor from a JSONObject
     * @param jsonObject JSONObject containing Anchor
     * @return A new Anchor
     */
    public static Anchor fromJSON(JSONObject jsonObject) {
        Orientation orientation = Orientation.getOrientation(jsonObject.getInt("orientation"));
        BlockLocation location = BlockLocation.fromJSON(jsonObject.getJSONArray("location"));
        return new Anchor(orientation,location);
    }

    /**
     * Gets the location of this Anchor
     * @return
     */
    public BlockLocation getLocation() {
        return location;
    }

    /**
     * Gets the Orientation of this Anchor
     * @return Orientation of this Anchor
     */
    public Orientation getOrientation() {
        return orientation;
    }
}
