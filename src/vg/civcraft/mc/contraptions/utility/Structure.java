package vg.civcraft.mc.contraptions.utility;

import vg.civcraft.mc.contraptions.utility.Anchor.Orientation;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
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
 */
public class Structure implements Iterable<Offset> {

    private final byte[][][] blocks;
    //Whether air blocks are a required part of the structure
    private final boolean ignoreAir;

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
     * @return If a structure exists at the locations
     */
    public boolean exists(BlockLocation location) {
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
        for (Offset offset : this) {
            if (!similiarBlocks(getMaterialID(offset), (byte) anchor.getBlock(offset).getTypeId())) {
                if (!(getMaterialID(offset) == 0 && ignoreAir)) {
                    return false;
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
     * TODO: Checks if a given locations is contained within structure
     *
     * @param anchor Anchor of the structure
     * @param location Location to check if it is contained in the structure
     * @return If the locations is contained in the structure
     */
    public boolean locationContainedInStructure(Anchor anchor, BlockLocation location) {
        return anchor.containedIn(location, getDimensions());
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
        for (Offset offset : this) {
            if (!(getMaterialID(offset) == 0 && ignoreAir)) {
                materials.add(getMaterial(offset));
            }
        }
        return materials;
    }

    /**
     * Gets the block at the specified offset
     *
     * @param offset Offset of block
     * @return Block at Offset
     */
    public Material getMaterial(Offset offset) {
        return Material.getMaterial(getMaterialID(offset));
    }

    /**
     * Gest the Material ID at the offset within this structure
     *
     * @param offset Offset of Material
     * @return Material ID at offset
     */
    public byte getMaterialID(Offset offset) {
        return blocks[offset.x][offset.y][offset.z];
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

    /**
     * Gets a collection of block locations occupied by this structure given the
     * Anchor
     *
     * @param anchor Anchor of Structure
     * @return Collection of occupied BlockLocations
     */
    public Collection<BlockLocation> getBlockLocations(Anchor anchor) {
        Collection<BlockLocation> locations = new ArrayList<BlockLocation>();
        for (Offset offset : this) {
            locations.add(anchor.getLocationOfOffset(offset));
        }
        return locations;
    }

    @Override
    public Iterator<Offset> iterator() {
        return new OffsetIterator();
    }

    private class OffsetIterator implements
            Iterator<Offset> {

        private int index;
        private final int size;
        private final int xLength;
        private final int yLength;
        private final int zLength;

        public OffsetIterator() {
            this.index = 0;
            int[] dimensions = getDimensions();
            xLength = dimensions[0];
            yLength = dimensions[1];
            zLength = dimensions[2];
            size = xLength * yLength * zLength;
        }

        @Override
        public boolean hasNext() {
            return this.index < size;
        }

        @Override
        public Offset next() {
            if (this.hasNext()) {
                Offset offset = new Offset(getX(), getY(), getZ());
                index++;
                return offset;
            }
            throw new NoSuchElementException();
        }

        private int getX() {
            return index % xLength;
        }

        private int getY() {
            return (index / xLength) % yLength;
        }

        private int getZ() {
            return ((index / xLength) / yLength) % zLength;
        }

        private int getSize() {
            return blocks.length * blocks[0].length * blocks[0][0].length;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
