package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.properties.FactoryProperties;
import com.untamedears.contraptions.utility.InventoryHelpers;
import com.untamedears.contraptions.utility.Response;
import org.bukkit.Location;
import org.json.JSONObject;

public class Factory extends Contraption {

    static String ENERGY_KEY = "Energy";
    Resource energy;

    public Factory(FactoryProperties properties, Location location) {
        super(properties, location);
        energy = new Resource(0, this);
        tasks.add(properties.getDecayGadget().run(energy));
    }

    @Override
    public JSONObject getResources() {
        JSONObject resources = new JSONObject();
        resources.put(ENERGY_KEY, energy);
        return resources;
    }

    @Override
    public void loadResources(JSONObject jsonObject) {
        energy = new Resource(jsonObject.getDouble(ENERGY_KEY),this);
    }

    @Override
    public Response trigger() {
        String prettyOutput = InventoryHelpers.toString(getProperties().getProductionGadget().getOutputs());
        if (getProperties().getProductionGadget().produceGoods(getInventory())) {
            return new Response(true, "Produced " + prettyOutput);
        }
        return new Response(false, "Cannot produce " + prettyOutput);
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
        return resourceID.equals(ENERGY_KEY);
    }

    @Override
    public Resource getResource(String resourceID) {
        return resourceID.equals(ENERGY_KEY) ? energy : null;
    }

}
