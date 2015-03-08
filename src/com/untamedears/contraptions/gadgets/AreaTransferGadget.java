package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.ContraptionsPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import java.util.Set;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/**
 * A gadget which will transfer a resource over time
 * <p>
 * This gadget transfers a resource from a contraption to all of the
 * contraptions surrounding it within a particular radius
 * <p>
 * Format of JSON object should be as follows:
 * <pre>
 * {
 *   "resourceID": "RESOURCE_ID",
 *   "radius": 1,
 *   "rate": 1
 * }
 * </pre>
 */
public class AreaTransferGadget {

    //The name of the which is being transfered to
    String resourceID;
    //Rate in amount per tick
    int radius;
    //Rate at which the transfer is occuring
    double rate;

    /**
     * Creates an AreaTransferGadget
     * <p>
     * @param resourceID String representing the resource this effects
     * @param radius     Square radius over which this effects contraptions
     * @param rate       Rate in 1/tick with which resources transfer from this
     *                   gadget
     */
    public AreaTransferGadget(String resourceID, int radius, double rate) {
        this.resourceID = resourceID;
        this.radius = radius;
        this.rate = rate;
    }

    /**
     * Creates a new AreaTransferGadget from a JSON config
     * <p>
     * @param jsonObject Contains the configuration information
     * @return The configured AreaTransferGadget
     */
    public static AreaTransferGadget fromJSON(JSONObject jsonObject) {
        String resourceID = jsonObject.getString("resourceID");
        int radius = jsonObject.getInt("radius");
        double rate = jsonObject.getDouble("rate");
        return new AreaTransferGadget(resourceID, radius, rate);
    }

    /**
     * Transfers a resource from a contraption to surrounding Contraptions
     * <p>
     * @param contraption Contraption being transfered from
     * @param resource    Resource being transfered from
     * @param time        # ticks of transfer to occur
     */
    public void areaTransfer(Contraption contraption, Resource resource, int time) {
        Set<Contraption> contraptions = getContraptions(contraption);
        int amount = (int) Math.floor(time * rate);
        for (Contraption otherContraption : contraptions) {
            if (otherContraption.hasResource(resourceID)) {
                safeTransfer(resource, otherContraption.getResource(resourceID), amount);
            }
        }
    }

    /**
     * Transfers from one resource to another with maximums and minimums
     * <p>
     * @param fromResource The resource that is being transferred from
     * @param toResource   The resource that is being transferred to
     * @param amount       The amount being transfered
     */
    public void safeTransfer(Resource fromResource, Resource toResource, int amount) {
        //Withdraw desired transfer amount from fromResource
        double transferedResource = -fromResource.safeChange(-amount, 0, Integer.MAX_VALUE);
        //Add as much of the widthdrawn amount as possible to toResource
        transferedResource -= toResource.safeChange(transferedResource, 0, Integer.MAX_VALUE);
        //Refund fromResource for untransferred amount
        fromResource.change(transferedResource);
    }

    /**
     * Creates a runnable associated with this gadget for a specific contraption
     * <p>
     * @param contraption Contraption associated with runnable
     * @param resource    Resource associated with runnable
     * @return The Task that was scheduled to run
     */
    public BukkitTask run(Contraption contraption, Resource resource) {
        return (new AreaTransferRunnable(contraption, resource)).runTaskTimerAsynchronously(ContraptionsPlugin.getContraptionPlugin(), 1000, 1000);
    }

    /**
     * Gets contraptions given this AreaTransferWidgets radius
     * <p>
     * @param contraption The contraption around which to get other contraptions
     * @return The Contraptions surrounding the given contraption
     */
    private Set<Contraption> getContraptions(Contraption contraption) {
        return contraption.getContraptionManager().getContraptions(contraption.getLocation(), radius);
    }

    class AreaTransferRunnable extends BukkitRunnable {

        //Associated Contraption
        Contraption contraption;
        //Decaying resource
        Resource resource;
        //Keeps track of the period of the task
        int period;

        /**
         * Creates an AreaTransferRunnable
         * <p>
         * @param contraption Contraption associated with the AreaTransfer
         * @param resource    Resource that is being transferred
         */
        public AreaTransferRunnable(Contraption contraption, Resource resource) {
            this.contraption = contraption;
            this.resource = resource;
        }

        /**
         * Schedules the task and decays the resource to the delay
         * <p>
         * @param Plugin The Contraptions Plugin
         * @param delay  The delay until the task is executed in ticks
         * @param period The period in ticks with which the task is executed
         */
        @Override
        public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
            areaTransfer(contraption, resource, (int) delay);
            this.period = (int) period;
            return super.runTaskTimer(plugin, delay, period);
        }

        /**
         * Transfers the resource and then checks that the contraption is valid
         */
        @Override
        public void run() {
            areaTransfer(contraption, resource, period);
            contraption.isValid();
        }
    }
}
