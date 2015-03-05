package com.untamedears.contraption.gadgets;


import com.untamedears.contraption.contraptions.Contraption;
import com.untamedears.contraption.Resource;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * A widget which will decay a resource over time
 */
public class LoadGadget extends BukkitRunnable {


    //Associated Contraption
    Contraption contraption;
    //Decaying resource
    Resource resource;
     //Rate in amount per tick
    List<Locations> locations;

    public LoadGadget (Contraption contraption, Resource resource, List<BlockFace> orientations){
        this.contraption = contraption;
        this.resource = resource;
        this.orientations = orientations;
    }
    
    public void loadItems() {
        Inventory inventory = contraption.getInventory();
        Location location = contraption.getLocation();
        for(BlockFace orientation:orientations) {
        }
    }

    /*
     * Called by bukkit scheduler
     */
    @Override
    public void run() {
        loadItems();
    }
}