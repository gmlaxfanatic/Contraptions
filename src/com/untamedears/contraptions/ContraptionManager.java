package com.untamedears.contraptions;

import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.properties.ContraptionProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class ContraptionManager {
    Plugin plugin;
    Set<ContraptionProperties> contraptionProperties;
    Map<Location,Contraption> contraptions;
    
    public ContraptionManager (Plugin plugin) {
        this.plugin = plugin;
        contraptions = new HashMap<Location,Contraption>();
        
    }
    
    /*
     * Gets a Contraption associated with the given location
     * If no Contraption exists null is returned
     */    
    public Contraption getContraption (Location location) {
        return contraptions.get(location);
        
    }
        
    /*
     * Iterates through all ContraptionProperties and attempts to create a factory
     * based at the contraption
     */
    public void createContraption (Location location) {
        Contraption contraption;
        for(ContraptionProperties contraptionProperty:contraptionProperties) {
            try {
                contraption = contraptionProperty.createContraption(location);
                //These lines only execute if there are no errors in contraption creation
                contraptions.put(location,contraption);
                return;
            }
            catch (Exception e) {
                
            }
        }
        
    }
    
    /*
     * Removes the contraption at a given location from being tracked by the manager
     * Returns true if the contraption was removed
    */
    public boolean destroy(Contraption contraption) {
        if(contraptions.containsKey(contraption.getLocation())) {
            contraptions.remove(contraption.getLocation());
            contraption.destroy();
            return true;
        }
        else {
            return false;
        }        
    }
    
    /*
     * Handles all events which may trigger a block
     */
    public void handleTriggeringEvent(Event e) {
        
    }
    
    /*
    * Handels instances of a block breaking
    * If a contraption existed at this block it is destroyed
    */
    public void handelBlockDestruction(Block block) {
         if(contraptions.containsKey(block.getLocation())) {
           destroy(contraptions.get(block.getLocation()));
        }
    }
}
