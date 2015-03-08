package com.untamedears.contraptions;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class ContraptionPlugin extends JavaPlugin {

    static ContraptionPlugin contraptionPlugin;
    ContraptionManager contraptionManager;

    @Override
    public void onEnable() {
        contraptionPlugin = this;
        contraptionManager = new ContraptionManager(this);
        try {
            contraptionManager.loadProperties(new File(getDataFolder(), "config.json"));
        } catch (Exception e) {
            throw e;
        }
        try {
            contraptionManager.loadContraptions(new File(getDataFolder(), "savefile.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerEvents();
    }

    @Override
    public void onDisable() {
        try {
            contraptionManager.saveContraptions(new File(getDataFolder(), "savefile.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers ContraptionListener
     */
    public void registerEvents() {
        try {
            getServer().getPluginManager().registerEvents(new ContraptionListener(contraptionManager), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows global access to this instance of plugin
     * <p>
     * @return The single instance of this plugin
     */
    public static ContraptionPlugin getContraptionPlugin() {
        return contraptionPlugin;
    }
}
