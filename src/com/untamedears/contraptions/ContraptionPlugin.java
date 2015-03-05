package com.untamedears.contraptions;

import com.untamedears.contraptions.ContraptionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ContraptionPlugin extends JavaPlugin
{

    static ContraptionPlugin contraptionPlugin;
    ContraptionManager contraptionManager;
    
    @Override
    public void onEnable()
    {

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
