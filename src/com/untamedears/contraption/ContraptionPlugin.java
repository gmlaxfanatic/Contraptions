package com.untamedears.contraption;

import com.untamedears.contraption.ContraptionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ContraptionPlugin extends JavaPlugin
{

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
}
