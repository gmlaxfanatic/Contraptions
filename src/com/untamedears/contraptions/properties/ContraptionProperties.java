package com.untamedears.contraptions.properties;

import com.untamedears.contraption.ContraptionManager;
import com.untamedears.contraption.contraptions.Contraption;
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
