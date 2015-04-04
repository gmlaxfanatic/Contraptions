package vg.civcraft.mc.contraptions.gadgets;

import vg.civcraft.mc.contraptions.ContraptionsPlugin;
import vg.civcraft.mc.contraptions.utility.Resource;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/**
 * A widget which will grow a resource over time
 * 
 * Format of JSON object should be as follows:
 * <pre>
 * {
 *   "rate": 1
 * }
 * </pre>
 */
public class GrowGadget {

    //Rate in amount per tick
    double rate;

    /**
     *
     * @param rate Rate of resource growth in 1/tick
     */
    public GrowGadget(double rate) {
        this.rate = rate;
    }

    /**
     * Imports a GrowGadget from a JSONObject
     * 
     * @param jsonObject The JSONObject containing the information
     * @return A GrowGadget with the properties contained in the JSONObject
     */
    public static GrowGadget fromJSON(JSONObject jsonObject) {
        double rate = jsonObject.getDouble("rate");
        return new GrowGadget(rate);
    }

    /**
     * Grows the resource by amount
     * 
     * @param resorce The resource being Grown
     * @param amount  A signed amount to change the resource by
     */
    public void grow(Resource resource, int amount, Resource scaler) {
        if(scaler == null) {
            resource.change(amount*rate);
        }
        else {
            resource.change(amount*rate*scaler.get());
        }
    }
    
    public void grow(Resource resource, int amount) {
        grow(resource, amount, null);
    }
}
