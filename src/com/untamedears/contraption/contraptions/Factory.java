package com.untamedears.contraption.contraptions;

import com.untamedears.contraption.Resource;
import com.untamedears.properties.FactoryProperties;
import com.untamedears.contraption.gadgets.DecayGadget;
import org.bukkit.Location;

public class Factory extends Contraption {

    DecayGadget decayGadget;
    Resource energy;

    public Factory(FactoryProperties properties, Location location) {
        super(properties, location);
        energy = new Resource(0,52594800);
        decayGadget = new DecayGadget(this,energy,1);
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

    @Override
    public void destroy() {
        try{
            decayGadget.cancel();
        }
        catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
