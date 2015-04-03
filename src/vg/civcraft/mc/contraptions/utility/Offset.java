package vg.civcraft.mc.contraptions.utility;

import vg.civcraft.mc.contraptions.utility.Anchor.Orientation;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.util.Vector;
import org.json.JSONObject;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;

/**
 * Represents a location within a structure
 */
public class Offset {

    final int x;
    final int y;
    final int z;

    /**
     * Creates an Offset
     *
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    public Offset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Rotates the positive offsets given an orientation on the XZ plane
     *
     * @param orientation Orientation to rotate with regards to
     * @return A new offset which has been reorientated
     */
    public Offset orient(Orientation orientation) {
        int newX = x * (orientation == Orientation.NW || orientation == Orientation.SE ? 0 : (orientation == Orientation.NE ? 1 : -1))
                + z * (orientation == Orientation.NE || orientation == Orientation.SW ? 0 : (orientation == Orientation.NW ? 1 : -1));
        int newZ = x * (orientation == Orientation.NE || orientation == Orientation.SW ? 0 : (orientation == Orientation.NW ? 1 : -1))
                + z * (orientation == Orientation.NW || orientation == Orientation.SE ? 0 : (orientation == Orientation.NE ? 1 : -1));
        return new Offset(newX, y, newZ);
    }

    /**
     * Converts the offset to a bukkit vector
     *
     * @return Vector representing this offset
     */
    public Vector toVector() {
        return new Vector(x, y, z);
    }

    /**
     * Returns the four anchor spots possible given a location
     *
     * @param location Location to offset
     * @return Potential anchors for offset
     */
    public Set<Anchor> getPotentialAnchors(BlockLocation location) {
        Set<Anchor> anchors = new HashSet<Anchor>();
        ContraptionsPlugin.toConsole("Potential anchors for " + location.toString());
        for (Orientation orientation : Orientation.values()) {
            Offset orientatedOffset = this.orient(orientation);
            anchors.add(new Anchor(orientation, new BlockLocation(location.getWorld(), location.x - orientatedOffset.x,
                    location.y - orientatedOffset.y, location.z - orientatedOffset.z)));
        }
        return anchors;
    }

    public static Offset fromJSON(JSONObject jsonObject) {
        int x = JSONHelpers.loadInt(jsonObject, "x", 0);
        int y = JSONHelpers.loadInt(jsonObject, "y", 0);
        int z = JSONHelpers.loadInt(jsonObject, "z", 0);
        return new Offset(x, y, z);
    }
}
