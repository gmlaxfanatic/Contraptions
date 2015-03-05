package com.untamedears.contraptions.properties;

import com.untamedears.contraptions.ContraptionManager;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.contraptions.Factory;
import com.untamedears.contraptions.gadgets.DecayGadget;
import com.untamedears.contraptions.gadgets.GenerationGadget;
import com.untamedears.contraptions.gadgets.ProductionGadget;
import com.untamedears.contraptions.utility.InventoryHelpers;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/*
 * A Factory Block
 * These blocks take a cost to build
 * require energy to keep operational
 * and enable the production of on set of items from another
 */
public class FactoryProperties extends ContraptionProperties {

    Set<ItemStack> match;
    ProductionGadget productionGadget;
    GenerationGadget generationGadget;
    DecayGadget decayGadget;
    Set<ItemStack> powerItems;

    public FactoryProperties(ContraptionManager contraptionManager, Set<ItemStack> match, Set<ItemStack> inputs, Set<ItemStack> outputs, Set<ItemStack> powerItems) {
        super(contraptionManager, Material.CHEST);
        this.match = match;
        this.productionGadget = new ProductionGadget(inputs, outputs);
        generationGadget = new GenerationGadget(powerItems, 4320000);
        this.powerItems = powerItems;
    }

    /*
     * Creates a factory contraption
     */
    @Override
    public Contraption createContraption(Location location) {
        //Fails if there is an incorrect block type
        if(!location.getBlock().getState().getType().equals(material)){
           throw new IllegalArgumentException("Invalid block for contraption") ;
        }
        Inventory inventory = ((InventoryHolder) location.getBlock().getState()).getInventory();
        if(InventoryHelpers.exactlyContained(inventory, match)) {
            InventoryHelpers.remove(inventory, match);
            return new Factory(this, location);
        }
        else {
            throw new IllegalArgumentException("Ingrediants do not match");
        }
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
