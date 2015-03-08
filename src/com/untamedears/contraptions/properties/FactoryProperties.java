package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.contraptions.Factory;
import com.untamedears.contraptions.gadgets.DecayGadget;
import com.untamedears.contraptions.gadgets.GenerationGadget;
import com.untamedears.contraptions.gadgets.MatchGadget;
import com.untamedears.contraptions.gadgets.ProductionGadget;
import com.untamedears.contraptions.utility.InventoryHelpers;
import com.untamedears.contraptions.utlity.Response;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

/*
 * A Factory Block
 * These blocks take a cost to build
 * require energy to keep operational
 * and enable the production of on set of items from another
 */
public class FactoryProperties extends ContraptionProperties {

    MatchGadget matchGadget;
    ProductionGadget productionGadget;
    GenerationGadget generationGadget;
    DecayGadget decayGadget;

    public FactoryProperties(ContraptionManager contraptionManager, String ID, MatchGadget matchGadget, ProductionGadget productionGadget, GenerationGadget generationGadget) {
        super(contraptionManager, ID, Material.CHEST);
        this.matchGadget = matchGadget;
        this.productionGadget = productionGadget;
        this.generationGadget = generationGadget;
    }

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

    public GenerationGadget getGenerationGadget() {
        return generationGadget;
    }

    public ProductionGadget getProductionGadget() {
        return productionGadget;
    }

    public DecayGadget getDecayGadget() {
        return decayGadget;
    }

}
