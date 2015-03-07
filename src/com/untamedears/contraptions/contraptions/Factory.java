package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.properties.FactoryProperties;
import org.bukkit.Location;
import org.json.JSONObject;

public class Factory extends Contraption {

    Resource energy;

    public Factory(FactoryProperties properties, Location location) {
        super(properties, location);
        energy = new Resource(0,this);
        tasks.add(properties.getDecayGadget().run(energy));
    }

    @Override
    public JSONObject getResources() {
        JSONObject resources = new JSONObject();
        resources.put("Energy", energy);
        return resources;
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
    public void update(Resource resource) {
        //If a change in energy triggered this update, check that energy is good
        if (resource == energy) {
            //If the energy has gone negative attempt to repower
            if (energy.get() < 0) {
                //Check if there are enough items in the factory to repower it
                if (getProperties().getGenerationGadget().canGenerate(-energy.get(), getInventory())) {
                    //repower the factory
                    getProperties().getGenerationGadget().generate(-energy.get(), getInventory(), energy);
                }
            }
            //If contraption ran out of energy destroy it
            if (energy.get() < 0) {
                getContraptionManager().destroy(this);
            }
        }
        update();
    }

    @Override
    public boolean hasResource(String resourceID) {
        return resourceID.equals("energy");
    }

    @Override
    public Resource getResource(String resourceID) {
        return resourceID.equals("energy") ? energy : null;
    }

}
