package vg.civcraft.mc.contraptions.properties;

import vg.civcraft.mc.contraptions.ContraptionManager;
import vg.civcraft.mc.contraptions.contraptions.Factory;
import vg.civcraft.mc.contraptions.gadgets.GrowGadget;
import vg.civcraft.mc.contraptions.gadgets.ConversionGadget;
import vg.civcraft.mc.contraptions.gadgets.MatchGadget;
import vg.civcraft.mc.contraptions.gadgets.MinMaxGadget;
import vg.civcraft.mc.contraptions.gadgets.ProductionGadget;
import vg.civcraft.mc.contraptions.utility.InventoryHelpers;
import vg.civcraft.mc.contraptions.utility.Response;
import vg.civcraft.mc.contraptions.utility.SoundType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import vg.civcraft.mc.contraptions.utility.org.json.JSONObject;

/**
 * The Properties associated with a Factory Contraption
 */
public class FactoryProperties extends ContraptionProperties {

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
     * @param matchGadget The MatchGadget associated with this specification
     * @param productionGadgets The ProductionGadget associated with this
     * specification
     * @param conversionGadget The ConversionGadget associated with this
     * specification
     */
    public FactoryProperties(ContraptionManager contraptionManager, String ID, String name, MatchGadget matchGadget, List<ProductionGadget> productionGadgets, ConversionGadget conversionGadget, GrowGadget growGadget, MinMaxGadget minMaxGadget) {
        super(contraptionManager, ID, name, Material.CHEST);
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
        String name = jsonObject.getString("name");
        MatchGadget matchGadget = new MatchGadget(InventoryHelpers.fromJSON(jsonObject.getJSONArray("building_materials")));
        List<ProductionGadget> productionGadgets = new ArrayList<ProductionGadget>();
        Iterator<String> productionGadgetNames = jsonObject.getJSONObject("recipes").keys();
        while(productionGadgetNames.hasNext()) {
            String productionGadgetName = productionGadgetNames.next();
            productionGadgets.add(ProductionGadget.fromJSON(productionGadgetName, jsonObject.getJSONObject("recipes").getJSONObject(productionGadgetName)));
        }
        ConversionGadget conversionGadget = new ConversionGadget(InventoryHelpers.fromJSON(jsonObject.getJSONArray("repair_materials")), jsonObject.getDouble("repair_amount"));
        GrowGadget growGadget = new GrowGadget(jsonObject.getDouble("breakdown_rate"));
        MinMaxGadget minMaxGadget = new MinMaxGadget(-Double.MAX_VALUE, jsonObject.getDouble("max_repair"));
        return new FactoryProperties(contraptionManager, ID, name, matchGadget, productionGadgets, conversionGadget, growGadget, minMaxGadget);
    }

    @Override
    public Factory newContraption(Location location) {
        return new Factory(this, location);
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
    public Response createContraption(Location location) {
        if (!validBlock(location.getBlock())) {
            return new Response(false, "Incorrect block for a Factory");
        }
        Inventory inventory = ((InventoryHolder) location.getBlock().getState()).getInventory();
        if (matchGadget.matches(inventory) && matchGadget.consume(inventory)) {
            Factory newFactory = new Factory(this, location);
            contraptionManager.registerContraption(newFactory);
            SoundType.CREATION.play(location);
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

}
