package vg.civcraft.mc.contraptions.properties;

import vg.civcraft.mc.contraptions.ContraptionManager;
import vg.civcraft.mc.contraptions.contraptions.Factory;
import vg.civcraft.mc.contraptions.gadgets.GrowGadget;
import vg.civcraft.mc.contraptions.gadgets.ConversionGadget;
import vg.civcraft.mc.contraptions.gadgets.MatchGadget;
import vg.civcraft.mc.contraptions.gadgets.MinMaxGadget;
import vg.civcraft.mc.contraptions.gadgets.ProductionGadget;
import vg.civcraft.mc.contraptions.utility.Response;
import vg.civcraft.mc.contraptions.utility.SoundType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;
import vg.civcraft.mc.contraptions.gadgets.StructureGadget;
import vg.civcraft.mc.contraptions.utility.Anchor;
import vg.civcraft.mc.contraptions.utility.BlockLocation;
import vg.civcraft.mc.contraptions.utility.InventoryHelpers;
import vg.civcraft.mc.contraptions.utility.JSONHelpers;

/**
 * The Properties associated with a Factory Contraption
 */
public class FactoryProperties extends ContraptionProperties {

    int period;
    MatchGadget matchGadget;
    List<ProductionGadget> productionGadgets;
    ConversionGadget conversionGadget;
    GrowGadget growGadget;
    MinMaxGadget minMaxGadget;

    /**
     * Creates a FactoryProperties object
     *
     * @param contraptionManager The ContraptionManager
     * @param ID The unique ID for this specification
     * @param name The displayed name of the Contraption
     * @param matchGadget The MatchGadget associated with this specification
     * @param productionGadgets The ProductionGadget associated with this
     * specification
     * @param conversionGadget The ConversionGadget associated with this
     * specification
     */
    public FactoryProperties(ContraptionManager contraptionManager, String ID, String name, StructureGadget structureGadget, int period, MatchGadget matchGadget, List<ProductionGadget> productionGadgets, ConversionGadget conversionGadget, GrowGadget growGadget, MinMaxGadget minMaxGadget) {
        super(contraptionManager, ID, name, structureGadget);
        this.period = period;
        this.matchGadget = matchGadget;
        this.productionGadgets = productionGadgets;
        this.conversionGadget = conversionGadget;
        this.growGadget = growGadget;
        this.minMaxGadget = minMaxGadget;
    }

    /**
     * Imports a FactoryProperties object from a configuration file
     *
     * @param contraptionManager The ContraptionManager
     * @param ID The Unique ID of this specification
     * @param jsonObject A JSONObject containing the specification
     * @return The specified FactoryProperties file
     */
    public static FactoryProperties fromConfig(ContraptionManager contraptionManager, String ID, JSONObject jsonObject) {
        String name = JSONHelpers.loadString(jsonObject, "name", ID);
        StructureGadget structureGadget;
        if (jsonObject.has("structure")) {
            structureGadget = StructureGadget.fromJSON(jsonObject);
        } else{
            structureGadget = ContraptionProperties.SG_DEFAULT;
        }
        //How frequently to update the generator in seconds
        int period = JSONHelpers.loadInt(jsonObject, "period", 600);
        //Creation recipe
        Set<ItemStack> matchGadgetItems = JSONHelpers.loadItemStacks(jsonObject, "building_materials");
        MatchGadget matchGadget = new MatchGadget(matchGadgetItems);
        //Production Recipes
        List<ProductionGadget> productionGadgets = new ArrayList<ProductionGadget>();
        Iterator<String> productionGadgetNames = jsonObject.getJSONObject("recipes").keys();
        while (productionGadgetNames.hasNext()) {
            String productionGadgetName = productionGadgetNames.next();
            productionGadgets.add(ProductionGadget.fromJSON(productionGadgetName, jsonObject.getJSONObject("recipes").getJSONObject(productionGadgetName)));
        }
        //Repair
        Set<ItemStack> repairMaterials = JSONHelpers.loadItemStacks(jsonObject, "repair_materials", InventoryHelpers.multiply(matchGadgetItems, InventoryHelpers.lcm(matchGadgetItems)));
        ConversionGadget conversionGadget = new ConversionGadget(repairMaterials, JSONHelpers.loadInt(jsonObject, "repair_amount", (int) (51840000 * 3.33333)));
        GrowGadget growGadget = new GrowGadget(JSONHelpers.loadDouble(jsonObject, "breakdown_rate", 1));
        MinMaxGadget minMaxGadget = new MinMaxGadget(-Double.MAX_VALUE, JSONHelpers.loadInt(jsonObject, "max_repair", 51840000));
        return new FactoryProperties(contraptionManager, ID, name, structureGadget, period, matchGadget, productionGadgets, conversionGadget, growGadget, minMaxGadget);
    }

    @Override
    public Factory newContraption(Anchor anchor) {
        return new Factory(this, anchor);
    }

    @Override
    public String getType() {
        return "Factory";
    }

    /**
     * Creates a Factory Contraptions
     *
     * @param location Location to attempt creation
     * @return Created Contraption if successful
     */
    @Override
    public Response createContraption(BlockLocation location) {
        Anchor anchor = structureGadget.exists(location);
        if (anchor == null) {
            return new Response(false, "Incorrect structure for factory");
        }
        Inventory inventory = ((InventoryHolder) location.getBlock().getState()).getInventory();
        if (matchGadget.matches(inventory) && matchGadget.consume(inventory)) {
            Factory newFactory = new Factory(this, anchor);
            contraptionManager.registerContraption(newFactory);
            SoundType.CREATION.play(newFactory.getLocation());
            return new Response(true, "Created a " + newFactory.getName() + " factory!", newFactory);
        }
        return new Response(false, "Incorrect items for a Factory");
    }

    /**
     * Gets the ConversionGadget
     *
     * @return The ConversionGadget
     */
    public ConversionGadget getConversionGadget() {
        return conversionGadget;
    }

    /**
     * Gets ProductionGadget List
     *
     * @return The ProductionGadget List
     */
    public List<ProductionGadget> getProductionGadgets() {
        return productionGadgets;
    }

    /**
     * Gets the GrowGadget
     *
     * @return The GrowGadget
     */
    public GrowGadget getGrowGadget() {
        return growGadget;
    }

    /**
     * Gets the MinMaxGadget
     *
     * @return The MinMaxGadget
     */
    public MinMaxGadget getMinMaxGadget() {
        return minMaxGadget;
    }

    public int getPeriod() {
        return period;
    }
}
