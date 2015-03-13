package com.untamedears.contraptions;

import com.untamedears.contraptions.utility.InventoryHelpers;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

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
    public static boolean PERMISSIONS = false;

    /**
     * Initializes the manager and loads config and save files
     */
    @Override
    public void onEnable() {
        contraptionPlugin = this;
        contraptionManager = new ContraptionManager(this);
        //Load in pretty item names
        toConsole("Loading materials.csv");
        InventoryHelpers.loadPrettyNames(new File(getDataFolder() + "materials.csv"));
        //Loading Property Files
        File configFolder = new File(getDataFolder() + "/configs");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
        //Load all files in the configs folder
        for (File configFile : configFolder.listFiles()) {
            ContraptionsPlugin.toConsole("Loading properties from " + configFile.getName());
            contraptionManager.loadProperties(configFile);
        }
        try {
            //Create temp file to store lost Contraptions
            File tempFile = File.createTempFile("lost_cotraptions", ".tmp", getDataFolder());
        } catch (Exception e) {
            toConsole("Failed to create temporary lost_contraptions.tmp file");
            e.printStackTrace();
        }

        Set<JSONObject> lostContraptions = new HashSet<JSONObject>();
        //Loading Contraptions
        File contraptionsFile = new File(getDataFolder(), "constraptions.json");
        if (contraptionsFile.exists()) {
            lostContraptions.addAll(contraptionManager.loadContraptions(contraptionsFile));
        }
        /*
         Loading Lost Contraptions, these are contraptions that once existed buth then their property file disappeared
         The most likely cause of this is a properties file failing to load
         */
        //Loading Contraptions
        File lostContraptionsFile = new File(getDataFolder(), "lost_contraptions.json");
        if (lostContraptionsFile.exists()) {
            lostContraptions.addAll(contraptionManager.loadContraptions(lostContraptionsFile));
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
