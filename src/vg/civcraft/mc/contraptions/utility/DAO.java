package vg.civcraft.mc.contraptions.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import vg.civcraft.mc.contraptions.contraptions.Contraption;

public class DAO {

    Map<BlockLocation,Contraption> contraptions;
    Map<BlockLocation,Collection<Contraption>> associatedContraptions;
    
    public DAO() {
        contraptions = new HashMap<BlockLocation,Contraption>();
        associatedContraptions = new HashMap<BlockLocation,Collection<Contraption>>();
    }

    /**
     * Gets a Contraption at a given location
     *
     * @param location Location of Contraption
     * @return Contraption at location, null if there isn't one
     */
    public Contraption getContraptionByLocation(BlockLocation location) {
        return contraptions.get(location);
    }

    /**
     * Gets all the contraptions associated with a given location
     *
     * This includes all contraptions whose structure bounding box overlaps with
     * the given location
     *
     * @param location Location of Contraptions
     * @return Collection of contraptions associated with location
     */
    public Collection<Contraption> getAssociatedContraptions(BlockLocation location) {
        return associatedContraptions.get(location);
    }

    /**
     * Inserts a Contraption
     *
     * @param contraption Contraption to register
     */
    public void registerContraption(Contraption contraption) {
        contraptions.put(contraption.getLocation(), contraption);
    }

    /**
     * Registers blocks associated with contraption
     *
     * @param contraption
     */
    public void registerAssociatedBlocks(Contraption contraption) {
        for(BlockLocation location:contraption.getBlockLocations()){
            if(associatedContraptions.containsKey(location)) {
                associatedContraptions.get(location).add(contraption);
            }
            else {
                Collection<Contraption> contraptions = new ArrayList<Contraption>();
                contraptions.add(contraption);
                associatedContraptions.put(location, contraptions);
            }
        }
    }
}
