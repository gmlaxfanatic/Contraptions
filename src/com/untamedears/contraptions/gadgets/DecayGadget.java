package com.untamedears.contraptions.gadgets;


import com.untamedears.contraptions.ContraptionPlugin;
import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.utility.Resource;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

/*
 * A widget which will decay a resource over time
 */
public class DecayGadget {

    //Rate in amount per tick
    double rate;

    public DecayGadget (double rate){
        this.rate = rate;
    }
    
    public BukkitTask run(Contraption contraption, Resource resource) {
        return (new DecayRunnable(contraption,resource)).runTaskTimerAsynchronously(ContraptionPlugin.getContraptionPlugin(), 1000, 1000);
    }
    
    /**
     * Imports a DecayGadget from a JSONObject
     * 
     * Format of JSON object should be as follows:
     * {
     *   "rate": 1
     * }
     * @param jsonObject The JSONObject containing the information
     * @return A DecayGadget with the properties contained in the JSONObject
     */
    public DecayGadget fromJSON(JSONObject jsonObject) {
        double rate = jsonObject.getDouble("rate");
        return new DecayGadget(rate);
    }
    
    /*
     * Decays the resource and calls the contraption to check that it is in
     * a good state
     */
    private void decay(Resource resource, int amount) {
        resource.change(-amount);
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