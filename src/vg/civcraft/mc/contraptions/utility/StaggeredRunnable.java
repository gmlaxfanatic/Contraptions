/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vg.civcraft.mc.contraptions.utility;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import vg.civcraft.mc.contraptions.ContraptionsPlugin;

/**
 *
 * @author Brian
 */
public abstract class StaggeredRunnable extends BukkitRunnable {

    /**
     * Schedules the task and grows the resource to the delay
     *
     * @param plugin The Contraptions Plugin
     * @param delay The delay until the task is executed in ticks
     * @param period The period in ticks with which the task is executed
     * @return This Task
     */
    @Override
    public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        execute((int) delay);
        return super.runTaskTimer(plugin, delay, period);
    }

    public synchronized BukkitTask runStaggeredTask(long period) throws IllegalArgumentException, IllegalStateException {
        return runTaskTimer(ContraptionsPlugin.getContraptionPlugin(), (long) (period * Math.random()), period);
    }

    /**
     * Grows the resource
     */
    @Override
    public void run() {
        execute(getPeriod());
    }

    protected abstract void execute(int duration);

    protected abstract int getPeriod();

}
