package vg.civcraft.mc.contraptions.gadgets;

import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;
import vg.civcraft.mc.contraptions.utility.InventoryHelpers;

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
    //Name of this ProductionGadget
    String name;

    /**
     * Creates a Production Gadget
     *
     * @param inputs The ItemStacks which are consumed
     * @param outputs The ItemStacks which are produced
     */
    public ProductionGadget(Set<ItemStack> inputs, Set<ItemStack> outputs, String name) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.name = name;
    }

    /**
     * Imports a ProductionGadget from a JSONObject
     *
     * @param name The name of the ProductionGadget
     * @param jsonObject The JSONObject containing the information
     * @return A ProductionGadget with the properties contained in the
     * JSONObject
     */
    public static ProductionGadget fromJSON(String name, JSONObject jsonObject) {
        Set<ItemStack> inputs = InventoryHelpers.fromJSON(jsonObject.getJSONArray("inputs"));
        Set<ItemStack> outputs = InventoryHelpers.fromJSON(jsonObject.getJSONArray("outputs"));
        return new ProductionGadget(inputs, outputs, name);
    }

    /**
     * Executed the Production function
     *
     * @param inventory
     * @return
     */
    public boolean produceGoods(Inventory inventory) {
        //Attempt to remove inputs from contraption
        if (InventoryHelpers.remove(inventory, inputs)) {
            InventoryHelpers.putIn(inventory, outputs);
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
    
    public String getName() {
        return name;
    }
}
