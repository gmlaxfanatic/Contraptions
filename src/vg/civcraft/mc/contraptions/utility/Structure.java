package vg.civcraft.mc.contraptions.utility;

import vg.civcraft.mc.contraptions.utility.Anchor.Orientation;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;
import vg.civcraft.mc.contraptions.utility.jnbt.ByteArrayTag;
import vg.civcraft.mc.contraptions.utility.jnbt.CompoundTag;
import vg.civcraft.mc.contraptions.utility.jnbt.NBTInputStream;
import vg.civcraft.mc.contraptions.utility.jnbt.ShortTag;
import vg.civcraft.mc.contraptions.utility.jnbt.Tag;

/**
 * Holds a defined three-dimensional rectangle of specified blocks
 *
 * Additionally provides methods for examening the structure within the context
 * of an anchor in the world.
 *
 */
public class Structure {

    private byte[][][] blocks;
    //Whether air blocks are a required part of the structure
    private boolean ignoreAir;

    /**
     * Creates a Structure
     *
     * @param blocks Bytes representing block IDs
     */
    public Structure(byte[][][] blocks) {
        this.blocks = blocks;
        this.ignoreAir = false;
    }

    /**
     * Checks if structure exists at a given point
     *
     * @param location Location in a world
     * @return If a structure exists at the location
     */
    public boolean exists(Location location) {
        for (Orientation orientation : Orientation.values()) {
            if (exists(new Anchor(orientation, location))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if structure exists at a given anchor point and given orientation
     *
     * @param anchor Anchor in a world
     * @return If a structure exists at the anchor
     */
    public boolean exists(Anchor anchor) {
        ContraptionsPlugin.toConsole("Blocks size. x:" + blocks.length + ", y:" + blocks[0].length + ", z:" + blocks[0][0].length);
        ContraptionsPlugin.toConsole("Testing for exisistance at anchor: " + anchor.getLocation().getBlockX() + ", " + anchor.getLocation().getY() + ", " + anchor.getLocation().getZ());
        for (int x = 0; x < blocks.length; x++) {
            ContraptionsPlugin.toConsole("Testing X for the " + x + " time.");
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    ContraptionsPlugin.toConsole("Testing Block: " + blocks[x][y][z]);
                    //Check if this is not a index contianing air which should be ignored
                    if (!(blocks[x][y][z] == 0 && ignoreAir)) {
                        ContraptionsPlugin.toConsole("Predicted Block: " + Material.getMaterial(blocks[x][y][z]).toString() + ". Actual Block: " + anchor.getLocation().clone().add(x * anchor.getXModifier(), y, z * anchor.getZModifier()).getBlock().getType().toString() + ". Location: " + anchor.getLocation().clone().add(x * anchor.getXModifier(), y, z * anchor.getZModifier()).toString());
                        if (!similiarBlocks(blocks[x][y][z], (byte) anchor.getLocation().clone().add(new Offset(x, y, z).orient(anchor.orientation).toVector()).getBlock().getTypeId())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;

    }

    /**
     * Compares two blocks and checks if they are the same
     *
     * Works with special cases such as burning furnaces
     */
    private boolean similiarBlocks(byte block, byte otherBlock) {
        return block == otherBlock || block == 61 && otherBlock == 62 || block == 62 && otherBlock == 61;
    }

    /**
     * TODO: Checks if a given location is contained within structure
     *
     * @param anchor Anchor of the structure
     * @param location Location to check if it is contained in the structure
     * @return If the location is contained in the structure
     */
    public boolean locationContainedInStructure(Anchor anchor, Location location) {
        return true;
    }

    /**
     * Checks if the given offset is within the bounds of the structure
     */
    private boolean validOffset(Offset offset) {
        return offset.x < blocks.length && offset.y < blocks[0].length && offset.z < blocks[0][0].length
                && offset.x >= 0 && offset.y >= 0 && offset.z >= 0;
    }

    /**
     * Gets all material used in this schematic
     *
     * @return The Materials used in this schematic
     */
    public Set<Material> getMaterials() {
        Set<Material> materials = new HashSet<Material>();
        for (short x = 0; x < blocks.length; x++) {
            for (short z = 0; z < blocks[x].length; z++) {
                for (short y = 0; y < blocks[x][z].length; y++) {
                    if (!(blocks[x][y][z] == 0 && ignoreAir)) {
                        materials.add(Material.getMaterial((int) blocks[x][z][y]));
                    }
                }
            }
        }
        return materials;
    }

    /**
     * Parses a Minecraft schematic file to a structure object
     *
     * @param file File to parse
     * @return Structure generated from file
     */
    public static Structure parseSchematic(File file) {
        try {
            NBTInputStream stream = new NBTInputStream(new FileInputStream(file));
            CompoundTag schematicTag = (CompoundTag) stream.readTag();
            Map<String, Tag> tags = schematicTag.getValue();
            short w = ((ShortTag) tags.get("Width")).getValue();
            short l = ((ShortTag) tags.get("Length")).getValue();
            short h = ((ShortTag) tags.get("Height")).getValue();
            byte[] importedBlocks = ((ByteArrayTag) tags.get("Blocks")).getValue();
            byte[][][] blocks = new byte[w][l][h];
            for (short x = 0; x < w; x++) {
                for (short z = 0; z < l; z++) {
                    for (short y = 0; y < l; y++) {
                        blocks[x][y][z] = importedBlocks[y * w * l + z * w + x];
                        ContraptionsPlugin.toConsole(String.valueOf(blocks[x][y][z]));
                    }
                }
            }
            stream.close();
            return new Structure(blocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Gets the dimensions of the structure
     *
     * @return The dimensions of the structure
     */
    public int[] getDimensions() {
        return new int[]{blocks.length, blocks[0].length, blocks[0][0].length};
    }
}
