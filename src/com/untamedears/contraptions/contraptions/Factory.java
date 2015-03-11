package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.properties.FactoryProperties;
import com.untamedears.contraptions.utility.InventoryHelpers;
import com.untamedears.contraptions.utility.Response;
import org.bukkit.Location;
import org.json.JSONObject;

/**
 * A Factory Contraption meant to replace the current ProductionFactory object
 * in FactoryMod.
 *
 * It is created by placing a specific set of Items in a chest and tapping it
 * with a stick. It then will start to lose energy slowly and requires fuel
 * items to be placed in the chest to re-energize it. The factory allows the
 * execution of a single recipe by placing the raw ingrediants in the chest and
 * then hitting the chest with a stick, which will allow the final produce to be
 * made
 */
public class Factory extends Contraption {

    static String ENERGY_KEY = "Energy";
    Resource energy;

    /**
     * Creates a Factory Contraption
     *
     * @param properties The Factory Properties Object
     * @param location   The Location of the Contraption
     */
    public Factory(FactoryProperties properties, Location location) {
        super(properties, location);
        energy = new Resource(0, this);
        tasks.add(properties.getGrowGadget().run(energy));
    }

    @Override
    public JSONObject getResources() {
        JSONObject resources = new JSONObject();
        resources.put(ENERGY_KEY, energy);
        return resources;
    }

    @Override
    public void loadResources(JSONObject jsonObject) {
        energy = new Resource(jsonObject.getDouble(ENERGY_KEY), this);
    }

    @Override
    public Response trigger() {
        String prettyOutput = InventoryHelpers.toString(getProperties().getProductionGadget().getOutputs());
        if (getProperties().getProductionGadget().produceGoods(getInventory())) {
            return new Response(true, "Produced " + prettyOutput, this);
        }
        return new Response(false, "Cannot produce " + prettyOutput, this);
    }

    @Override
    protected FactoryProperties getProperties() {
        return (FactoryProperties) properties;
    }

    @Override
    public void update(Resource resource) {
        //If a change in energy triggered this update, check that energy is good
        if (resource == energy) {
            getProperties().getMinMaxGadget().update(resource);
            //If the energy has gone to less than 10% attempt to repower it
            if (energy.get() < getProperties().getMinMaxGadget().getMax() * 0.1) {
                //Check if there are enough items in the factory to repower it
                if (getProperties().getConversionGadget().canGenerate(-energy.get(), getInventory())) {
                    //repower the factory
                    getProperties().getConversionGadget().generate(-energy.get(), getInventory(), energy);
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
