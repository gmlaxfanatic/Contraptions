package com.untamedears.contraptions;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Contraptions goal is to provide a framework for adding increased
 * functionality to blocks in minecraft. At its core it is meant to be highly
 * flexible to the addition of new functional elements to individual minecraft
 * blocks, allowing for the simple creation of a wide range of different kinds
 * of interactive structures. This program will allow tens of thousands of
 * interactive blocks to be added to the minecraft world in an efficient manner
 * giving rise to emergent constructions of complex machinery.
 */
public class ContraptionsPlugin extends JavaPlugin {

    static ContraptionsPlugin contraptionPlugin;
    ContraptionManager contraptionManager;

    /**
     * Initializes the manager and loads config and save files
     */
    @Override
    public void onEnable() {
        contraptionPlugin = this;
        contraptionManager = new ContraptionManager(this);
        try {
            File propertiesFile = new File(getDataFolder(), "config.json");
            ContraptionsPlugin.toConsole(getDataFolder().getCanonicalPath());
            if (!propertiesFile.exists()) {
                propertiesFile.getParentFile().mkdirs();
                propertiesFile.createNewFile();
            }
            contraptionManager.loadProperties(propertiesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File contraptionsFile = new File(getDataFolder(), "savefile.json");
            if (contraptionsFile.exists()) {
                contraptionManager.loadContraptions(contraptionsFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerEvents();
    }

    /**
     * Save the contraptions
     */
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
            getServer().getPluginManager().registerEvents(new ContraptionsListener(contraptionManager), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows global access to this instance of plugin
     * 
     * @return The single instance of this plugin
     */
    public static ContraptionsPlugin getContraptionPlugin() {
        return contraptionPlugin;
    }

    /**
     * Sends a message to the console
     * 
     * @param message The message
     */
    public static void toConsole(String message) {
        Bukkit.getLogger().info("[Contraptions] " + message);
    }
}
