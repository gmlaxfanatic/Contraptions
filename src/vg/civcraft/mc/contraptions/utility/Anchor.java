package vg.civcraft.mc.contraptions.utility;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Represents a location with a orientation
 */
class Anchor implements Serializable {

    //Describes the oritentation of the structure

    enum Orientation {

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

    final Orientation orientation;
    final int x;
    final int y;
    final int z;
    final String worldName;

    Anchor(Orientation orientation, Location location) {

        this.orientation = orientation;
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
        worldName = location.getWorld().getName();
    }

    /**
     * Gets the location of the Anchor
     * 
     * @return A Location object representing this Anchor
     */
    Location getLocation() {
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
    }

    /**
     * Gets either a negative or positive increment of X depending on its
     * orientation
     * 
     * @return A direction of this anchor 
     */
    int getXModifier() {
        return orientation == Orientation.NE || orientation == Orientation.NW ? 1 : -1;
    }

    /**
     * Gets either a negative or positive increment of X depending on its
     * orientation
     * 
     * @return Z direction of this anchor
     */
    int getZModifier() {
        return orientation == Orientation.NE || orientation == Orientation.SE ? 1 : -1;
    }

    /**
     * Returns a location the offset given this anchor
     * 
     * @param offset An offset from the anchor
     * @return Location representing the location of the Offset
     */
    Location getLocationOfOffset(Offset offset) {
        Offset orientedOffset = offset.orient(orientation);
        return getLocation().add(orientedOffset.x, orientedOffset.y, orientedOffset.z);
    }

    /**
     * Gets the block located in the world given the offset and this anchor
     * 
     * @return The Block at the given offset
     */
    Block getBlock(Offset offset) {
        return getLocationOfOffset(offset).getBlock();
    }

    /**
     * Check if location is contained within a bounding box at this anchor
     * 
     * @return If the location is contained within the bounding box
     */
    boolean containedIn(Location testLocation, int[] dimensions) {
        Location location = getLocation();
        if ((testLocation.getBlockX() - location.getBlockX()) < dimensions[0] * getXModifier()) {
            if ((testLocation.getBlockZ() - location.getBlockZ()) < dimensions[2] * getZModifier()) {
                if (0 <= (testLocation.getBlockZ() - location.getBlockZ()) && (testLocation.getBlockZ() - location.getBlockZ()) < dimensions[1]) {
                    return true;
                }
            }
        }
        return false;
    }
}
