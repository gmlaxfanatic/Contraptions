package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.utility.Resource;
import com.untamedears.contraptions.utility.InventoryHelpers;
import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*
A Gadget which consumes itemstacks in order to generate more of a particular
resource
*/

public class GenerationGadget {
    
    //ItemStacks consumed to generate resouce
    Set<ItemStack> itemStacks;
    //The amount of resource generated with each item set
    int conversion;
    
    public GenerationGadget(Set<ItemStack> itemStacks, int conversion) {
        this.itemStacks = itemStacks;
        this.conversion = conversion;
    }
    
    /*
    Checks if this is capable of generating an amount of the resource
    */
    public boolean canGenerate(int amount, Inventory inventory, Resource resource) {
        int amountAvailible = InventoryHelpers.amountAvailable(inventory, itemStacks);
        return amountAvailible*conversion >= amount;
    }
    
    /*
    consume the minimum multiples of ItemSets to produce the amount of resources
    */
    public boolean generate(int amount, Inventory inventory, Resource resource) {
        int numberOfSets = (int) Math.ceil(amount/(double)conversion);
        if(InventoryHelpers.removeMultiple(inventory, itemStacks, numberOfSets)) {
            resource.change(numberOfSets*conversion);
            return true;
        }
        return false;
    }
    
}
