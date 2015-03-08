package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.utility.InventoryHelpers;
import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

/**
 * A gadget which only consumes ItemStacks if they are the only ones present
 * <p>
 * This can be used to specify a contraption being build
 * <p>
 * It can be imported from a JSON object in the following format
 * <pre>
 * {
 *   "itemstacks":
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
public class MatchGadget {

    //ItemStacks consumed to generate resouce
    Set<ItemStack> itemStacks;

    /**
     * Creates a GenerationGadget
     * <p>
     * @param itemStacks The ItemStacks consumed
     */
    public MatchGadget(Set<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

    /**
     * Imports a MatchGadget from a JSONObject
     * <p>
     * @param jsonObject The JSONObject containing the information
     * @return A MatchGadget with the properties contained in the JSONObject
     */
    public static MatchGadget fromJSON(JSONObject jsonObject) {
        Set<ItemStack> itemStacks = InventoryHelpers.fromJSON(jsonObject.getJSONArray("itemstacks"));
        return new MatchGadget(itemStacks);
    }

    /**
     * Given an inventory checks exactly ItemStacks is contained
     * <p>
     * @param inventory The inventory to pull ItemStacks from
     * @return Checks if ItemStacks are exactly in the Inventory
     */
    public boolean matches(Inventory inventory) {
        return InventoryHelpers.exactlyContained(inventory, itemStacks);
    }

    /**
     * Consumes ItemSets to generate a resource
     * <p>
     * @param inventory The inventory from which to draw ItemStacks
     * @return IF consuming was successful
     */
    public boolean consume(Inventory inventory) {
        return InventoryHelpers.remove(inventory, itemStacks);
    }
}
