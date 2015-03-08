package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.ContraptionPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utlity.Response;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Contains the properties associated with a particular implmentation of
 * a Contraption
 */
public abstract class ContraptionProperties {

    ContraptionManager contraptionManager;
    protected Material material;
    String ID;
    String name;

    public ContraptionProperties(ContraptionManager contraptionManager, String ID, Material material) {
        this.contraptionManager = contraptionManager;
        this.ID = ID;
        this.material = material;

    }
    
    public JSONObject save(Contraption contraption) {
        JSONObject saveJSON = new JSONObject();
        saveJSON.put("Type",getType());
        saveJSON.put("Properties",getID());
        String location = "["+contraption.getLocation().getWorld().getUID()+","+
                contraption.getLocation().getBlockX()+","+
                contraption.getLocation().getBlockY()+","+
                contraption.getLocation().getBlockZ()+"]";
        saveJSON.put("Location",location);
        saveJSON.put("Resources",contraption.getResources());
        return saveJSON;
    }
    /*
     * Initializes the Contraption
     * confirms that all of the creation conditions are met
     */
    public abstract Response createContraption(Location location);
    
    public abstract String getType();
    
    public abstract Contraption newContraption(Location location);
    
    
    public Contraption loadContraption(JSONObject jsonObject){
        JSONArray locationArray = jsonObject.getJSONArray("Location");
        Location location = new Location(ContraptionPlugin.getContraptionPlugin().getServer().getWorld(locationArray.getString(0)),
                locationArray.getInt(1),locationArray.getInt(2),locationArray.getInt(3));
        Contraption contraption = newContraption(location);
        contraption.loadResources(jsonObject.getJSONObject("Resources"));
        return contraption;
    }

    public boolean validBlock(Block block){
        return block.getState().getType().equals(material);
    }
    
    public ContraptionManager getContraptionManager() {
        return contraptionManager;
    }

    public String getID() {
        return ID;
    }

    public Material getMaterial() {
        return material;
    }
}
