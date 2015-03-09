package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.utility.InventoryHelpers;
import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

/**
 * A gadget the produces a set of ItemStacks from another set of ItemStacks
 * 
 * It can be imported from a config with the following JSON object:
 * <pre>
 * {
 *   "inputs":
 *     [{
 *         "material": "MATERIAL_NAME",
 *         "amount": 1,
 *         "durability": 0,
 *         "name": "DISPLAY_NAME",
 *         "lore": "LORE"
 *       },...
 *       }],
 *   "outputs":
 *     [{
 *         "material": "MATERIAL_NAME",
 *         "amount": 1,
 *         "durability": 0,
 *         "name": "DISPLAY_NAME",
 *         "lore": "LORE"
 *       },...
 *       }]
 * }
 * </pre>
 */

public class ProductionGadget {
    
    //Set of items used as inputs
    Set<ItemStack> inputs;
    //Set of items used as outputs
    Set<ItemStack> outputs;
    
    /**
     * Creates a Production Gadget
     * 
     * @param inputs The ItemStacks which are consumed
     * @param outputs The ItemStacks which are produced
     */
    public ProductionGadget(Set<ItemStack> inputs, Set<ItemStack> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }
    
    /**
     * Imports a ProductionGadget from a JSONObject
     * 
     * @param jsonObject The JSONObject containing the information
     * @return A ProductionGadget with the properties contained in the JSONObject
     */
    public static ProductionGadget fromJSON(JSONObject jsonObject) {
        Set<ItemStack> inputs = InventoryHelpers.fromJSON(jsonObject.getJSONArray("inputs"));
        Set<ItemStack> outputs = InventoryHelpers.fromJSON(jsonObject.getJSONArray("outputs"));
        return new ProductionGadget(inputs, outputs);
    }

    /**
     * Executed the Production function
     * 
     * @param inventory
     * @return
     */
    
    public boolean produceGoods(Inventory inventory){
        //Attempt to remove inputs from contraption
        if(InventoryHelpers.remove(inventory, inputs)){
            InventoryHelpers.putIn(inventory,outputs);
            return true;
        }
        return false;
    }
    
    public Set<ItemStack> getInputs() {
        return inputs;
    }
    public Set<ItemStack> getOutputs() {
        return outputs;
    }
}