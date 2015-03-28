package vg.civcraft.mc.contraptions.utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;

public class BlockLocation {

    public final World world;
    public final int x;
    public final int y;
    public final int z;

    public BlockLocation(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockLocation(String world, int x, int y, int z) {
        this(ContraptionsPlugin.getContraptionPlugin().getServer().getWorld(world), x, y, z);
    }

    public BlockLocation(Location location) {
        this(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public BlockLocation add(int x, int y, int z) {
        return new BlockLocation(world, this.x + x, this.y + y, this.z + z);
    }

    public BlockLocation add(Offset offset) {
        return new BlockLocation(world, this.x + offset.x, this.y + offset.y, this.z + offset.z);
    }

    public Block getBlock() {
        return (new Location(world, x, y, z)).getBlock();
    }

    public World getWorld() {
        return world;
    }

    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
