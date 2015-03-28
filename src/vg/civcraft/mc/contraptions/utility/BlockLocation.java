package vg.civcraft.mc.contraptions.utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.json.JSONArray;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;

/**
 * Represents a location of a block in a Minecraft world
 */
public class BlockLocation {

    /**
     * World of block
     */
    public final World world;

    /**
     * X coordinate of Bloc
     */
    public final int x;

    /**
     * Y coordinate of Block
     */
    public final int y;

    /**
     * Z coordinate of Block
     */
    public final int z;

    /**
     * Create a Block Location
     *
     * @param world Minecraft world
     * @param x X coordinate of Block
     * @param y Y coordinate of Block
     * @param z Z coordinate of Block
     */
    public BlockLocation(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a Block Location
     *
     * @param world A name of a Minecraft world
     * @param x X coordinate of Block
     * @param y Y coordinate of Block
     * @param z Z coordinate of Block
     */
    public BlockLocation(String world, int x, int y, int z) {
        this(ContraptionsPlugin.getContraptionPlugin().getServer().getWorld(world), x, y, z);
    }

    /**
     * Creates a BlockLocation
     *
     * @param location A Bukkit Location of the BlockLocation
     */
    public BlockLocation(Location location) {
        this(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Returns a new BlockLocation by adding to this Block Location
     *
     * @param x Amount in X direction
     * @param y Amount in Y direction
     * @param z Amount in Z direction
     * @return A new BlockLocation
     */
    public BlockLocation add(int x, int y, int z) {
        return new BlockLocation(world, this.x + x, this.y + y, this.z + z);
    }

    /**
     * Returns a new BlockLocation with the coordinates of this BlockLocation
     * added to it
     *
     * @param blockLocation Other BlockLocation
     * @return A new BlockLocation
     */
    public BlockLocation add(BlockLocation blockLocation) {
        return add(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
    }

    /**
     * Returns a new BlockLocation with Offset add to it
     *
     * @param offset Offset to add
     * @return A new BlockLocation
     */
    public BlockLocation add(Offset offset) {
        return add(offset.x, offset.y, offset.z);
    }

    /**
     * Gets the Block associated with this BlockLocation
     *
     * @return Block of BlockLocation
     */
    public Block getBlock() {
        return world.getBlockAt(x, y, z);
    }

    /**
     * Gets this BlockLocation's World
     *
     * @return The World of this BlockLocation
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the analogous Bukkit Location of this BlockLocation
     *
     * @return A Bukkit Location
     */
    public Location getBukkitLocation() {
        return new Location(world, x, y, z);
    }

    /**
     * Gets the X coordinate of this BlockLocation
     *
     * @return The X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of this BlockLocation
     *
     * @return The Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the Z coordinate of this BlockLocation
     *
     * @return The Z coordinate
     */
    public int getZ() {
        return z;
    }
    
    public static BlockLocation fromJSON(JSONArray jsonArray) {
        World world = ContraptionsPlugin.getContraptionPlugin().getServer().getWorld(jsonArray.getString(0));
        return new BlockLocation(world,jsonArray.getInt(1),jsonArray.getInt(2),jsonArray.getInt(3));
    }
}
