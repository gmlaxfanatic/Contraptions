package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.ContraptionPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/**
 * A widget which will decay a resource over time
 *
 *      * Format of JSON object should be as follows:
 * <pre>
 * {
 *   "rate": 1
 * }
 * </pre>
 */
public class DecayGadget {

    //Rate in amount per tick
    double rate;

    /**
     *
     * @param rate Rate of rousrce dey in 1/tick
     */
    public DecayGadget(double rate) {
        this.rate = rate;
    }

    /**
     * Creates a runnable associated with this gadget for a specific contraption
     *
     * @param contraption Contraption which owns the resource
     * @param resource    Resource being decayed
     * @return The DecayGadget Runnable
     */
    public BukkitTask run(Contraption contraption, Resource resource) {
        return (new DecayRunnable(contraption, resource)).runTaskTimerAsynchronously(ContraptionPlugin.getContraptionPlugin(), 1000, 1000);
    }

    /**
     * Imports a DecayGadget from a JSONObject
     *
     * @param jsonObject The JSONObject containing the information
     * @return A DecayGadget with the properties contained in the JSONObject
     */
    public DecayGadget fromJSON(JSONObject jsonObject) {
        double rate = jsonObject.getDouble("rate");
        return new DecayGadget(rate);
    }

    /**
     * Decays the resource by amount
     * @param resorce The resource being decayed
     * @param amount A signed amount to change the resource by
     */
    private void decay(Resource resource, int amount) {
        resource.change(-amount);
    }

    class DecayRunnable extends BukkitRunnable {

        //Associated Contraption
        Contraption contraption;
        //Decaying resource
        Resource resource;
        //Keeps track of the period of the task
        int period;
        
        /**
         * The DecayGadget runnable associated with a contraption
         *
         * @param contraption The associated contraption
         * @param resource The resource being decayed
         */
        public DecayRunnable(Contraption contraption, Resource resource) {
            this.contraption = contraption;
            this.resource = resource;
        }

        /**
         * Schedules the task and decays the resource to the delay
         * 
         * @param Plugin The Contraptions Plugin
         * @param delay The delay until the task is executed in ticks
         * @param period The period in ticks with which the task is executed
         */
        @Override
        public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
            decay(resource, (int) delay);
            this.period = (int) period;
            return super.runTaskTimer(plugin, delay, period);
        }

        /**
         * Decays the resource and then checks that the contraption is valid
         */
        @Override
        public void run() {
            decay(resource, period);
            contraption.isValid();
        }
    }
}
