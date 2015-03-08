package com.untamedears.contraptions;

import org.bukkit.plugin.java.JavaPlugin;

public class ContraptionPlugin extends JavaPlugin
{

    static ContraptionPlugin contraptionPlugin;
    ContraptionManager contraptionManager;
    
    @Override
    public void onEnable()
    {
        contraptionPlugin = this;
        contraptionManager = new ContraptionManager(this);
        contraptionManager.onEnable();
        registerEvents();
    }

    @Override
    public void onDisable()
    {

    }

    public void registerEvents()
    {
            try
            {
                    getServer().getPluginManager().registerEvents(new ContraptionListener(contraptionManager), this);
            }
            catch(Exception e)
            {
                    e.printStackTrace();
            }
    }
    
    public static ContraptionPlugin getContraptionPlugin() {
        return contraptionPlugin;
    }
}
