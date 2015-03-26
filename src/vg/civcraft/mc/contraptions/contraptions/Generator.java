package vg.civcraft.mc.contraptions.contraptions;

import vg.civcraft.mc.contraptions.properties.FactoryProperties;
import vg.civcraft.mc.contraptions.properties.GeneratorProperties;
import vg.civcraft.mc.contraptions.utility.Resource;
import org.bukkit.Location;
import org.json.JSONObject;

public class Generator extends Contraption{

    static String ENERGY_KEY = "energy";
    static String TERRITORY_KEY = "territory";
    Resource energy;
    Resource territory;
    Resource generated;

    /**
     * Creates a Generator Contraption
     *
     * @param properties The Factory Properties Object
     * @param location   The Location of the Contraption
     */
    public Generator(GeneratorProperties properties, Location location) {
        super(properties, location);
        energy = new Resource(0, this);
        territory = new Resource(0,this);
        generated = new Resource(0,this);
        tasks.add(properties.getGrowGadget().run(energy));
        tasks.add(properties.getConversionGadget().getConvertToItemStacksRunnable(this, generated));
    }

    @Override
    public JSONObject getResources() {
        JSONObject resources = new JSONObject();
        resources.put(ENERGY_KEY, energy.get());
        resources.put(TERRITORY_KEY, territory.get());
        return resources;
    }

    @Override
    public void loadResources(JSONObject jsonObject) {
        energy = new Resource(jsonObject.getDouble(ENERGY_KEY), this);
        territory = new Resource(jsonObject.getDouble(TERRITORY_KEY), this);
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
                if (getProperties().getConversionGadget().canConvertToResource(-energy.get(), getInventory())) {
                    //repower the factory
                    getProperties().getConversionGadget().convertToResource(-energy.get(), getInventory(), energy);
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
        return resourceID.equals(ENERGY_KEY)||resourceID.equals(TERRITORY_KEY);
    }

    @Override
    public Resource getResource(String resourceID) {
        if(resourceID.equals(ENERGY_KEY) ){
            return energy;
        }
        if(resourceID.equals(TERRITORY_KEY)){
            return territory;
        }
        return null;
    }

}