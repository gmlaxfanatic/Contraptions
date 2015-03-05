package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.utility.InventoryHelpers;
import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*
 * A module used to produce one set of items given a second set of items
 */
public class ProductionGadget {
    
    //Set of items used as inputs
    Set<ItemStack> inputs;
    //Set of items used as outputs
    Set<ItemStack> outputs;
    
    public ProductionGadget(Set<ItemStack> inputs, Set<ItemStack> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }
        
    /*
     * Attempts to exchange the inputs for the ouputs of the functional block
     * returns whether or not it was successful
     */
    public boolean produceGoods(Inventory inventory){
        //Attempt to remove inputs from contraption
        if(InventoryHelpers.remove(inventory, inputs)){
            InventoryHelpers.putIn(inventory,outputs);
            return true;
        }
        return false;
    }
    
}