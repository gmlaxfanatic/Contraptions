package com.untamedears.properties;

import com.untamedears.contraption.ContraptionManager;
import com.untamedears.contraption.contraptions.Contraption;
import com.untamedears.contraption.contraptions.Factory;
import com.untamedears.contraption.gadgets.GenerationGadget;
import com.untamedears.contraption.gadgets.ProductionGadget;
import com.untamedears.contraption.utility.InventoryHelpers;
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

}
