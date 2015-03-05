package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.properties.FactoryProperties;
import org.bukkit.Location;

public class Factory extends Contraption {

    Resource energy;

    public Factory(FactoryProperties properties, Location location) {
        super(properties, location);
        energy = new Resource(0,52594800);
        tasks.add(properties.getDecayGadget().startDecay(this, energy));
    }
    
    @Override
    public boolean trigger() {
        getProperties().getProductionGadget().produceGoods(getInventory());
        return false;
    }
    
    @Override
    protected FactoryProperties getProperties() {
        return (FactoryProperties) properties;
    }

    @Override
    public void update() {
        //Check energy, consume more if needed
        if(!isValid()) {
            getContraptionManager().destroy(this);
        }
        //If the energy has gone negative attempt to repower
        if(energy.getResource() < 0){
            //Check if there are enough items in the factory to repower it
            if(getProperties().getGenerationGadget().canGenerate(-energy.getResource(), getInventory(), energy)) {
                //repower the factory
                getProperties().getGenerationGadget().generate(-energy.getResource(), getInventory(), energy);
            }
        }
        //If contraption ran out of energy destroy it
        if(energy.getResource() < 0) {
            getContraptionManager().destroy(this);
        }
    }
}
