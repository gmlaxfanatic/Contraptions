package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.ContraptionsPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/**
 * A widget which will grow a resource over time
 * <p>
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
     * Creates a runnable associated with this gadget with constant growth
     * <p>
     * @param resource Resource being grown
     * @return The GrowGadget Runnable
     */
    public BukkitTask run(Resource resource) {
        return run(resource, null);
    }

    /**
     * Creates a runnable associated with this gadget with scaled growth
     * <p>
     * @param scaler   Scaler to growth rate
     * @param resource Resource being grown
     * @return The GrowGadget Runnable
     */
    public BukkitTask run(Resource resource, Resource scaler) {
        return (new GrowRunnable(resource, scaler)).runTaskTimerAsynchronously(ContraptionsPlugin.getContraptionPlugin(), 1000, 1000);
    }

    /**
     * Imports a GrowGadget from a JSONObject
     * <p>
     * @param jsonObject The JSONObject containing the information
     * @return A GrowGadget with the properties contained in the JSONObject
     */
    public static GrowGadget fromJSON(JSONObject jsonObject) {
        double rate = jsonObject.getDouble("rate");
        return new GrowGadget(rate);
    }

    /**
     * Grows the resource by amount
     * <p>
     * @param resorce The resource being Grown
     * @param amount  A signed amount to change the resource by
     */
    private void grow(Resource resource, int amount, Resource scaler) {
        if(scaler == null) {
            resource.change(amount*rate);
        }
        else {
            resource.change(amount*rate*scaler.get());
        }
    }

    class GrowRunnable extends BukkitRunnable {

        //Growing resource
        Resource resource;
        //Scaler
        Resource scaler;
        //Keeps track of the period of the task
        int period;

        /**
         * The GrowGadget runnable associated with a contraption
         * <p>
         * @param resource The resource being grown
         * @param scaler   A resource that scales the growth rate
         */
        public GrowRunnable(Resource resource, Resource scaler) {
            this.resource = resource;
            this.scaler = resource;
        }

        /**
         * Schedules the task and grows the resource to the delay
         * <p>
         * @param Plugin The Contraptions Plugin
         * @param delay  The delay until the task is executed in ticks
         * @param period The period in ticks with which the task is executed
         */
        @Override
        public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
            grow(resource, (int) delay, scaler);
            this.period = (int) period;
            return super.runTaskTimer(plugin, delay, period);
        }

        /**
         * Grows the resource
         */
        @Override
        public void run() {
            grow(resource, period, scaler);
        }
    }
}
