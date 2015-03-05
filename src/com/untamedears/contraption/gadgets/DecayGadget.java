package com.untamedears.contraption.gadgets;


import com.untamedears.contraption.contraptions.Contraption;
import com.untamedears.contraption.Resource;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/*
 * A widget which will decay a resource over time
 */
public class DecayGadget extends BukkitRunnable {


    //Associated Contraption
    Contraption contraption;
    //Decaying resource
    Resource resource;
     //Rate in amount per tick
    double rate;
    //Keeps track of the period of the task
    int period;

    public DecayGadget (Contraption contraption, Resource resource, double rate){
        this.contraption = contraption;
        this.resource = resource;
        this.rate = rate;
    }
    
    /*
     * Decays the resource and calls the contraption to check that it is in
     * a good state
     */
    private void decay(int amount) {
        resource.changeResource(-amount);
        contraption.isValid();
    }
    
    /*
     * Overrides the bukkit scheduler to register the start and delay of the run
     */
    @Override
    public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException  {
        decay((int)delay);
        this.period = (int) period;
        return super.runTaskTimer(plugin, delay, period);
    }

    /*
     * Called by bukkit scheduler
     */
    @Override
    public void run() {
        decay(period);
    }
}