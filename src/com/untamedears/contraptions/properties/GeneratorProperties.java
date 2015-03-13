package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Factory;
import com.untamedears.contraptions.contraptions.Generator;
import com.untamedears.contraptions.gadgets.ConversionGadget;
import com.untamedears.contraptions.gadgets.GrowGadget;
import com.untamedears.contraptions.gadgets.MatchGadget;
import com.untamedears.contraptions.gadgets.MinMaxGadget;
import com.untamedears.contraptions.gadgets.ProductionGadget;
import com.untamedears.contraptions.gadgets.TerritoryGadget;
import com.untamedears.contraptions.utility.InventoryHelpers;
import com.untamedears.contraptions.utility.Response;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.json.JSONObject;

public class GeneratorProperties extends ContraptionProperties{
   MatchGadget matchGadget;
   TerritoryGadget territoryGadget;
    ConversionGadget conversionGadget;
    GrowGadget growGadget;
    MinMaxGadget minMaxGadget;

    /**
     * Creates a GeneratorProperties object
     *
     * @param contraptionManager The ContraptionManager
     * @param ID                 The unique ID for this specification
     * @param matchGadget        The MatchGadget associated with this
     *                           specification
     * @param territoryGadget   The TerritoryGadget associated with this
     *                           specification
     * @param conversionGadget   The ConversionGadget associated with this
     *                           specification
     */
    public GeneratorProperties(ContraptionManager contraptionManager, String ID, MatchGadget matchGadget, TerritoryGadget territoryGadget, ConversionGadget conversionGadget, GrowGadget growGadget, MinMaxGadget minMaxGadget) {
        super(contraptionManager, ID, Material.CHEST);
        this.matchGadget = matchGadget;
        this.territoryGadget = territoryGadget;
        this.conversionGadget = conversionGadget;
        this.growGadget = growGadget;
        this.minMaxGadget = minMaxGadget;
    }

    /**
     * Imports a GeneratorProperties object from a configuration file
     *
     * @param contraptionManager The ContraptionManager
     * @param ID                 The Unique ID of this specification
     * @param jsonObject         A JSONObject containing the specification
     * @return The specified FactoryProperties file
     */
    public static GeneratorProperties fromConfig(ContraptionManager contraptionManager, String ID, JSONObject jsonObject) {
        MatchGadget matchGadget = new MatchGadget(InventoryHelpers.fromJSON(jsonObject.getJSONArray("building_materials")));
        TerritoryGadget territoryGadget = new TerritoryGadget();
        ConversionGadget conversionGadget = new ConversionGadget(InventoryHelpers.fromJSON(jsonObject.getJSONArray("repair_materials")),jsonObject.getDouble("repair_amount"));
        GrowGadget growGadget = new GrowGadget(jsonObject.getDouble("breakdown_rate"));
        MinMaxGadget minMaxGadget = new MinMaxGadget(-Double.MAX_VALUE,jsonObject.getDouble("max_repair"));
        return new GeneratorProperties(contraptionManager, ID, matchGadget, territoryGadget, conversionGadget, growGadget, minMaxGadget);
    }

    @Override
    public Generator newContraption(Location location) {
        return new Generator(this, location);
    }

    @Override
    public String getType() {
        return "generator";
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
            Generator newGenerator = new Generator(this, location);
            contraptionManager.registerContraption(newGenerator);
            return new Response(true, "Created a " + newGenerator.getName() + " factory!", newGenerator);
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
    public TerritoryGadget getTerritoryGadget() {
        return territoryGadget;
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
