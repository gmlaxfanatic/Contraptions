package vg.civcraft.mc.contraptions;

import static vg.civcraft.mc.contraptions.ContraptionsPlugin.toConsole;
import vg.civcraft.mc.contraptions.utility.InventoryHelpers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import org.json.JSONWriter;

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
    @SuppressWarnings("CallToPrintStackTrace")
    public void onEnable() {
        contraptionPlugin = this;
        contraptionManager = new ContraptionManager(this);

        //Load in pretty item names
        toConsole("Loading materials.csv");
        this.saveResource("materials.csv", true);
        InventoryHelpers.loadPrettyNames(new File(getDataFolder() + "/materials.csv"));

        //Loading Property Files
        File configFolder = new File(getDataFolder() + "/configs");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
        //Load all files in the configs folder
        for (File configFile : configFolder.listFiles()) {
            contraptionManager.loadProperties(configFile);
        }

        //Loading Contraptions
        Set<JSONObject> lostContraptions = new HashSet<JSONObject>();
        File contraptionsFile = new File(getDataFolder(), "contraptions.json");
        if (contraptionsFile.exists()) {
            lostContraptions.addAll(contraptionManager.loadContraptions(contraptionsFile));
        }
        //Copy contraptions file over to old file
        try {
            replaceOldFile(contraptionsFile);
        } catch (IOException e) {
            toConsole("Failed to replace " + contraptionsFile.getName());
        }
        /*
         * Loading Lost Contraptions, these are contraptions that once existed
         * but then their property file disappeared The most likely cause of
         * this is a properties file failing to load
         */
        //Try to load lost Contraptions
        File lostContraptionsFile = new File(getDataFolder(), "lost_contraptions.json");
        if (lostContraptionsFile.exists()) {
            lostContraptions.addAll(contraptionManager.loadContraptions(lostContraptionsFile));
        }
        //Copy lost contraptions file to old version
        try {
            replaceOldFile(lostContraptionsFile);
        } catch (IOException e) {
            toConsole("Failed to replace " + lostContraptionsFile.getName());
        }
        //Save new Set of lost Contraptions
        try {
            ContraptionsPlugin.toConsole("Saving lost contraptions...");
            FileOutputStream fileOutputStream = new FileOutputStream(lostContraptionsFile);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            JSONWriter jsonWriter = new JSONWriter(bufferedWriter);
            jsonWriter.array();
            for (JSONObject contraption : lostContraptions) {
                jsonWriter.value(contraption);
            }
            jsonWriter.endArray();
            bufferedWriter.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            toConsole("Failed to save lost contraptions");
            e.printStackTrace();
        }
        //Register Listeners
        registerEvents();
    }

    /**
     * Save the contraptions
     */
    @Override
    public void onDisable() {
        try {
            contraptionManager.saveContraptions(new File(getDataFolder(), "contraptions.json"));
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

    public static void replaceOldFile(File sourceFile) throws IOException {
        File destFile = new File(sourceFile.getCanonicalPath() + ".old");
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
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
