package vg.civcraft.mc.contraptions.utility;

import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

public class JSONHelpers {

    public static int loadInt(JSONObject object, String key, int alt) {
        if (object.has(key)) {
            return object.getInt(key);
        }
        return alt;
    }

    public static double loadDouble(JSONObject object, String key, double alt) {
        if (object.has(key)) {
            return object.getDouble(key);
        }
        return alt;
    }

    public static String loadString(JSONObject object, String key, String alt) {
        if (object.has(key)) {
            return object.getString(key);
        }
        return alt;
    }

    public static Set<ItemStack> loadItemStacks(JSONObject object, String key) {
        return InventoryHelpers.fromJSON(object.getJSONArray(key));

    }

    public static Set<ItemStack> loadItemStacks(JSONObject object, String key, Set<ItemStack> alt) {
        if (object.has(key)) {
            try {
                return InventoryHelpers.fromJSON(object.getJSONArray(key));
            } catch (Exception e) {

            }
        }
        return alt;
    }
}
