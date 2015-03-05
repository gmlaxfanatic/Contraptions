package com.untamedears.contraption.gadgets;


import com.untamedears.contraption.ContraptionPlugin;
import com.untamedears.contraption.contraptions.Contraption;
import com.untamedears.contraption.Resource;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/*
 * A widget which will decay a resource over time
 */
public class DecayGadget {

    //Rate in amount per tick
    double rate;

    public DecayGadget (double rate){
        this.rate = rate;
    }
    
    public BukkitTask startDecay(Contraption contraption, Resource resource) {
        return (new DecayRunnable(contraption,resource)).runTaskTimerAsynchronously(ContraptionPlugin.getContraptionPlugin(), 1000, 1000);
    }
    
    /*
     * Decays the resource and calls the contraption to check that it is in
     * a good state
     */
    private void decay(Resource resource, int amount) {
        resource.changeResource(-amount);
    }    
    
    class DecayRunnable extends BukkitRunnable
    {
        //Associated Contraption
        Contraption contraption;
        //Decaying resource
        Resource resource;
        //Keeps track of the period of the task
        int period;
        
        public DecayRunnable(Contraption contraption, Resource resource) {
            this.contraption = contraption;
            this.resource = resource;
        }
        
        /*
        * Overrides the bukkit scheduler to register the start and delay of the run
        */
       @Override
       public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException  {
           decay(resource, (int)delay);
           this.period = (int) period;
           return super.runTaskTimer(plugin, delay, period);
       }

        @Override
        public void run() {
            decay(resource, period);
            contraption.isValid();
        }
    }
}