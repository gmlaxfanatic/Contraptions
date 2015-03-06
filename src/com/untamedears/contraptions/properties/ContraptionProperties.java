package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import org.bukkit.Location;
import org.bukkit.Material;

/*
 * Contains the properties associated with a particular implmentation of
 * a Contraption
 */
public abstract class ContraptionProperties {

    ContraptionManager contraptionManager;
    protected Material material;

    public ContraptionProperties(ContraptionManager contraptionManager, Material material) {
        this.contraptionManager = contraptionManager;
        this.material = material;
    }

    /*
     * Initializes the Contraption
     * confirms that all of the creation conditions are met
     */
    public abstract Contraption createContraption(Location location);

    public ContraptionManager getContraptionManager() {
        return contraptionManager;
    }

    public Material getMaterial() {
        return material;
    }
}
