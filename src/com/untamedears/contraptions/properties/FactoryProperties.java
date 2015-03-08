package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Factory;
import com.untamedears.contraptions.gadgets.GrowGadget;
import com.untamedears.contraptions.gadgets.GenerationGadget;
import com.untamedears.contraptions.gadgets.MatchGadget;
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
    GenerationGadget generationGadget;
    GrowGadget growGadget;

    /**
     * Creates a FactoryProperties object
     * @param contraptionManager The ContraptionManager
     * @param ID The unique ID for this specification
     * @param matchGadget The MatchGadget associated with this specification
     * @param productionGadget The ProductionGadget associated with this specification
     * @param generationGadget The GenerationGadget associated with this specification
     */
    public FactoryProperties(ContraptionManager contraptionManager, String ID, MatchGadget matchGadget, ProductionGadget productionGadget, GenerationGadget generationGadget) {
        super(contraptionManager, ID, Material.CHEST);
        this.matchGadget = matchGadget;
        this.productionGadget = productionGadget;
        this.generationGadget = generationGadget;
    }

    /**
     *Imports a FactoryProperties object from a configuration file
     * @param contraptionManager The ContraptionManager
     * @param ID The Unique ID of this specification
     * @param jsonObject A JSONObject containing the specification
     * @return The specified FactoryProperties file
     */
    public static FactoryProperties fromConfig(ContraptionManager contraptionManager, String ID, JSONObject jsonObject) {
        MatchGadget matchGadget = MatchGadget.fromJSON(jsonObject.getJSONObject("MatchGadget"));
        ProductionGadget productionGadget = ProductionGadget.fromJSON(jsonObject.getJSONObject("MatchGadget"));
        GenerationGadget generationGadget = GenerationGadget.fromJSON(jsonObject.getJSONObject("MatchGadget"));
        return new FactoryProperties(contraptionManager, ID, matchGadget, productionGadget, generationGadget);
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
     * <p>
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
            return new Response(true, "Created a " + newFactory.getName() + " factory!");
        }
        return new Response(false, "Incorrect items for a Factory");
    }

    /**
     *Gets the GenerationGadget
     * @return The GenerationGadget
     */
    public GenerationGadget getGenerationGadget() {
        return generationGadget;
    }

    /**
     * Gets the ProductionGadget
     * @return The ProductionGadget
     */
    public ProductionGadget getProductionGadget() {
        return productionGadget;
    }

    /**
     * Gets the GrowGadget
     * @return The GrowGadget
     */
    public GrowGadget getGrowGadget() {
        return growGadget;
    }

}
