package com.untamedears.contraptions.gadgets;

import com.untamedears.contraptions.ContraptionPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import java.util.Set;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AreaTransferWidget {
    
    //The name of the which is being transfered to
    String resourceID;
    //Rate in amount per tick
    int radius;
    //Rate at which the transfer is occuring
    double rate;

    public AreaTransferWidget (String resourceID, int radius, double rate){
        this.resourceID = resourceID;
        this.radius = radius;
        this.rate = rate;
    }
    
    public void areaTransfer(Contraption contraption, Resource resource, int time) {
        Set<Contraption> contraptions = getContraptions(contraption);
        int amount = (int) Math.floor(time*rate);
        for(Contraption otherContraption:contraptions) {
            if(otherContraption.hasResource(resourceID)) {
                safeTransfer(resource,otherContraption.getResource(resourceID), amount);
            }
        }
    }
    /**
     * Transfers from one resource to another with maximums and minimums
     * 
     * @param fromResource The resource that is being transferred from
     * @param toResource The resource that is being transferred to
     * @param amount     The amount being transfered
     */
    public void safeTransfer(Resource fromResource, Resource toResource,int amount) {
        //Withdraw desired transfer amount from fromResource
        int transferedResource = -fromResource.safeChange(-amount, 0, Integer.MAX_VALUE);
        //Add as much of the widthdrawn amount as possible to toResource
        transferedResource -= toResource.safeChange(transferedResource, 0, Integer.MAX_VALUE);
        //Refund fromResource for untransferred amount
        fromResource.change(transferedResource);
    }
    
    public BukkitTask run(Contraption contraption, Resource resource) {
        return (new AreaTransferRunnable(contraption,resource)).runTaskTimerAsynchronously(ContraptionPlugin.getContraptionPlugin(), 1000, 1000);
    }
    
    /**
     * Gets contraptions given this AreaTransferWidgets radius
     * 
     * @param contraption   The contraption around which to get other contraptions
     */
    private Set<Contraption> getContraptions(Contraption contraption) {
        return contraption.getContraptionManager().getContraptions(contraption.getLocation(), radius);
    }    
    
    class AreaTransferRunnable extends BukkitRunnable
    {
        //Associated Contraption
        Contraption contraption;
        //Decaying resource
        Resource resource;
        //Keeps track of the period of the task
        int period;
        
        public AreaTransferRunnable(Contraption contraption, Resource resource) {
            this.contraption = contraption;
            this.resource = resource;
        }
        
        /*
        * Overrides the bukkit scheduler to register the start and delay of the run
        */
       @Override
       public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException  {
           areaTransfer(contraption, resource, (int)delay);
           this.period = (int) period;
           return super.runTaskTimer(plugin, delay, period);
       }

        @Override
        public void run() {
            areaTransfer(contraption, resource, period);
            contraption.isValid();
        }
    }
}
