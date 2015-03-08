package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utlity.Response;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        saveJSON.put("Location",contraption.getLocation().serialize());
        saveJSON.put("Resources",contraption.getResources());
        return saveJSON;
    }
    /*
     * Initializes the Contraption
     * confirms that all of the creation conditions are met
     */
    public abstract Response createContraption(Location location);
    
    public abstract String getType();

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
