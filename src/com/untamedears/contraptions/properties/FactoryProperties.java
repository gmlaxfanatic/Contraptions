package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Factory;
import com.untamedears.contraptions.gadgets.GrowGadget;
import com.untamedears.contraptions.gadgets.ConversionGadget;
import com.untamedears.contraptions.gadgets.MatchGadget;
import com.untamedears.contraptions.gadgets.MinMaxGadget;
import com.untamedears.contraptions.gadgets.ProductionGadget;
import com.untamedears.contraptions.utility.Response;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.json.JSONObject;

/**
 * The Properties associated with a Factory Contraption
 */
public class FactoryProperties extends ContraptionProperties {

    MatchGadget matchGadget;
    ProductionGadget productionGadget;
    ConversionGadget conversionGadget;
    GrowGadget growGadget;
    MinMaxGadget minMaxGadget;

    /**
     * Creates a FactoryProperties object
     *
     * @param contraptionManager The ContraptionManager
     * @param ID                 The unique ID for this specification
     * @param matchGadget        The MatchGadget associated with this
     *                           specification
     * @param productionGadget   The ProductionGadget associated with this
     *                           specification
     * @param conversionGadget   The ConversionGadget associated with this
     *                           specification
     */
    public FactoryProperties(ContraptionManager contraptionManager, String ID, MatchGadget matchGadget, ProductionGadget productionGadget, ConversionGadget conversionGadget, MinMaxGadget minMaxGadget) {
        super(contraptionManager, ID, Material.CHEST);
        this.matchGadget = matchGadget;
        this.productionGadget = productionGadget;
        this.conversionGadget = conversionGadget;
        this.minMaxGadget = minMaxGadget;
    }

    /**
     * Imports a FactoryProperties object from a configuration file
     *
     * @param contraptionManager The ContraptionManager
     * @param ID                 The Unique ID of this specification
     * @param jsonObject         A JSONObject containing the specification
     * @return The specified FactoryProperties file
     */
    public static FactoryProperties fromConfig(ContraptionManager contraptionManager, String ID, JSONObject jsonObject) {
        MatchGadget matchGadget = MatchGadget.fromJSON(jsonObject.getJSONObject("building_materials"));
        ProductionGadget productionGadget = ProductionGadget.fromJSON(jsonObject.getJSONObject("recipe"));
        ConversionGadget conversionGadget = ConversionGadget.fromJSON(jsonObject.getJSONObject("repairs"));
        MinMaxGadget minMaxGadget = MinMaxGadget.fromJSON(jsonObject.getJSONObject("repair_bounds"));
        return new FactoryProperties(contraptionManager, ID, matchGadget, productionGadget, conversionGadget, minMaxGadget);
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
     * Gets the ProductionGadget
     *
     * @return The ProductionGadget
     */
    public ProductionGadget getProductionGadget() {
        return productionGadget;
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
