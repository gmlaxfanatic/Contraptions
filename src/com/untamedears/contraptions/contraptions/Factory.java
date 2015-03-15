package com.untamedears.contraptions.contraptions;

import com.untamedears.contraptions.gadgets.ProductionGadget;
import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.properties.FactoryProperties;
import com.untamedears.contraptions.utility.Response;
import com.untamedears.contraptions.utility.SoundType;
import org.bukkit.Location;
import org.json.JSONArray;
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
        resources.put(ENERGY_KEY, energy.get());
        return resources;
    }

    @Override
    public void loadResources(JSONObject jsonObject) {
        energy = new Resource(jsonObject.getDouble(ENERGY_KEY), this);
    }
    
    @Override
    public Response trigger() {
        JSONArray message = new JSONArray();
        message.put((new JSONObject()).put("text","You are using a "+properties.getName()+". Click on a recipe to use it:").put("color", "gray"));
        message.put((new JSONObject()).put("text", "\\n|").put("color","gray"));
        for(ProductionGadget productionGadget: getProperties().getProductionGadgets()) {
            JSONObject recipe = new JSONObject();
            recipe.put("text", productionGadget.getName());
            recipe.put("color", "yellow");
            JSONObject clickEvent = new JSONObject();
            clickEvent.put("action","run_command");
            clickEvent.put("value", "/say hi");
            recipe.put("clickEvent", clickEvent);
            message.put(recipe);
            message.put((new JSONObject()).put("text", "|").put("color","gray"));
        }
        return new Response(true, message.toString(),this);
    }
    
    public Response trigger(int i) {
        if (getProperties().getProductionGadgets().get(i).produceGoods(getInventory())) {
            SoundType.PRODUCTION.play(location);
            return new Response(true, "Produced ", this);
        }
        return new Response(false, "Cannot produce ", this);
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
