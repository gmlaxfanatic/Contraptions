package vg.civcraft.mc.contraptions.contraptions;

import vg.civcraft.mc.contraptions.properties.FactoryProperties;
import vg.civcraft.mc.contraptions.properties.GeneratorProperties;
import vg.civcraft.mc.contraptions.utility.Resource;
import org.json.JSONObject;
import vg.civcraft.mc.contraptions.utility.Anchor;
import vg.civcraft.mc.contraptions.utility.StaggeredRunnable;

/**
 * A Generator creates a resource based upon how much land it controls. When the
 * resource exceeds a maximum it will be converted into an ItemStack.
 * Conversely, when the resource gets below 20% it will consume the resource to
 * stay alive. Over time the resource will also automatically shrink.
 */
public class Generator extends Contraption {

    static String ESSENCE_KEY = "essence";
    static String TERRITORY_KEY = "territory";
    Resource essence;
    Resource territory;

    /**
     * Creates a Generator Contraption
     *
     * @param properties The Factory Properties Object
     * @param anchor The Anchor of the Contraption
     */
    public Generator(GeneratorProperties properties, Anchor anchor) {
        super(properties, anchor);
        essence = new Resource(properties.getMinMaxGadget().getMax(), this);
        territory = new Resource(0, this);
        tasks.add(new GeneratorRunnable().runStaggeredTask(properties.getPeriod()));
        properties.getTerritoryGadget().addContraptions(this, territory);
    }

    @Override
    public void update() {
        /*
         If essence exceeds maximum convert to ItemStacks
         */
        double max = getProperties().getMinMaxGadget().getMax();
        getProperties().getConversionGadget().ConvertDownToResource(essence, getInventory(), max);
        /*
         If essence is below 20%*maximum convert from ItemStacks
         */
        getProperties().getConversionGadget().convertUpToResource(essence, getInventory(), max * 0.2);
        //If essence is below minimum destroy generator
        if (essence.get() < getProperties().getMinMaxGadget().getMin()) {
            destroy();
        }
    }

    class GeneratorRunnable extends StaggeredRunnable {

        @Override
        protected void execute(int duration) {
            //Grow essence based on territory controlled
            getProperties().getGenerationGadget().grow(essence, duration,territory);
            //Shrink essence based on duration of execution
            getProperties().getDegradationGadget().grow(essence, duration);
            update();
        }

        @Override
        protected int getPeriod() {
            return getProperties().getPeriod();
        }
    }

    @Override
    public JSONObject getResources() {
        JSONObject resources = new JSONObject();
        resources.put(ESSENCE_KEY, essence.get());
        resources.put(TERRITORY_KEY, territory.get());
        return resources;
    }

    @Override
    public void loadResources(JSONObject jsonObject) {
        essence = new Resource(jsonObject.getDouble(ESSENCE_KEY), this);
        territory = new Resource(jsonObject.getDouble(TERRITORY_KEY), this);
    }

    @Override
    protected GeneratorProperties getProperties() {
        return (GeneratorProperties) properties;
    }

    @Override
    public boolean hasResource(String resourceID) {
        return resourceID.equals(ESSENCE_KEY) || resourceID.equals(TERRITORY_KEY);
    }

    @Override
    public Resource getResource(String resourceID) {
        if (resourceID.equals(ESSENCE_KEY)) {
            return essence;
        }
        if (resourceID.equals(TERRITORY_KEY)) {
            return territory;
        }
        return null;
    }

}
