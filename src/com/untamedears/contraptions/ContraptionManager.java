package com.untamedears.contraptions;

import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.properties.ContraptionProperties;
import com.untamedears.contraptions.properties.FactoryProperties;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ContraptionManager {

    Plugin plugin;
    Map<String,ContraptionProperties> contraptionProperties;
    Map<Location, Contraption> contraptions;

    public ContraptionManager(Plugin plugin) {
        this.plugin = plugin;
        //There has gotta be a better way to do this
        
        try {
            contraptionProperties = loadProperties(new File(plugin.getDataFolder(),"config.json"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads the ContraptionProperties from the Config File
     * <p>
     * @param contraptionProperties
     */
    private static Map<String,ContraptionProperties> loadProperties(File file) {
        Map<String,ContraptionProperties> contraptionProperties = new HashMap<String,ContraptionProperties>();
        try{
            JSONTokener tokener = new JSONTokener(new FileReader(file));
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject factories = jsonObject.getJSONObject("Factory");
            for(String ID:factories.keySet()){
                contraptionProperties.put(ID, FactoryProperties.fromConfig(factories.getJSONObject(ID)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return contraptionProperties;
    }

    /*
     * Gets a Contraption associated with the given location
     * If no Contraption exists null is returned
     */
    public Contraption getContraption(Location location) {
        return contraptions.get(location);

    }

    /**
     * Gets contraptions located within a square around the given location
     * <p>
     * @param location Central location
     * @param radius   Square radius from which to search
     * @return Set of contraptions in radius
     */
    public Set<Contraption> getContraptions(Location location, int radius) {
        Set<Contraption> contraptions = new HashSet<Contraption>();
        Contraption contraption;
        Location currentLocation = location.clone();

        for (int x = -radius; x <= radius; x++) {
            currentLocation.setX(x);
            for (int z = -radius; z <= radius; z++) {
                currentLocation.setZ(z);
                contraption = getContraption(currentLocation);
                if (contraption != null) {
                    contraptions.add(contraption);
                }
            }
        }
        return contraptions;
    }

    /*
     * Iterates through all ContraptionProperties and attempts to create a factory
     * based at the contraption
     */
    public void createContraption(Location location) {
        Contraption contraption;
        for (ContraptionProperties contraptionProperty : contraptionProperties) {
            try {
                contraption = contraptionProperty.createContraption(location);
                //These lines only execute if there are no errors in contraption creation
                contraptions.put(location, contraption);
                return;
            } catch (Exception e) {

            }
        }

    }

    /*
     * Removes the contraption at a given location from being tracked by the manager
     * Returns true if the contraption was removed
     */
    public boolean destroy(Contraption contraption) {
        if (contraptions.containsKey(contraption.getLocation())) {
            contraptions.remove(contraption.getLocation());
            contraption.destroy();
            return true;
        } else {
            return false;
        }
    }

    /*
     * Handles all events which may trigger a block
     */
    public void handleTriggeringEvent(Event e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Handels instances of a block breaking
     * If a contraption existed at this block it is destroyed
     */
    public void handelBlockDestruction(Block block) {
        if (contraptions.containsKey(block.getLocation())) {
            destroy(contraptions.get(block.getLocation()));
        }
    }
}
