package vg.civcraft.mc.contraptions;

import vg.civcraft.mc.contraptions.contraptions.Contraption;
import vg.civcraft.mc.contraptions.properties.ContraptionProperties;
import vg.civcraft.mc.contraptions.properties.FactoryProperties;
import vg.civcraft.mc.contraptions.utility.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;
import vg.civcraft.mc.citadel.Citadel;
import vg.civcraft.mc.citadel.ReinforcementManager;
import vg.civcraft.mc.citadel.reinforcement.PlayerReinforcement;
import vg.civcraft.mc.namelayer.permission.PermissionType;

/**
 * Manages access, loading, and saving of Contraptions
 */
public class ContraptionManager {

    Plugin plugin;
    Map<String, ContraptionProperties> contraptionProperties;
    Map<Location, Contraption> contraptions;
    ReinforcementManager reinforcementManager;

    /**
     * Creates a ContraptionManager
     *
     * @param plugin
     */
    public ContraptionManager(Plugin plugin) {
        this.plugin = plugin;
        if (ContraptionsPlugin.PERMISSIONS) {
            reinforcementManager = Citadel.getReinforcementManager();
        }
        contraptionProperties = new HashMap<String, ContraptionProperties>();
        contraptions = new HashMap<Location, Contraption>();
    }

    /**
     * Loads the ContraptionProperties from the Config File
     *
     * @param file File containing the properties
     */
    public void loadProperties(File file) {
        try {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new FileReader(file)));
            //Go there all Contraption implementations here and load them individually
            //Specifically loads Factory Contraptions
            if (jsonObject.has("factory")) {
                JSONObject factories = jsonObject.getJSONObject("factory");
                for (String ID : factories.keySet()) {
                    try {
                        contraptionProperties.put(ID, FactoryProperties.fromConfig(this, ID, factories.getJSONObject(ID)));
                    } catch (Exception e) {
                        ContraptionsPlugin.toConsole("Failed to load Factory properties: " + ID);
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            ContraptionsPlugin.toConsole("Could not load properties file: " + file.getName());
        }
    }

    /**
     * Loads the Contraptions from a file
     *
     * Contraptions should be formatted as following:
     * <pre>
     * [
     *   {
     *     "ID": "FactoryTypeA",
     *     "Location": [0, 0, 0],
     *     "Resources": {
     *       "resource1:": 0
     *     }
     *   },
     *   {
     *     ...
     *   }
     * ]
     * </pre>
     *
     * @param file File to be loaded
     */
    public Set<JSONObject> loadContraptions(File file) {
        Set<JSONObject> lostContraptions = new HashSet<JSONObject>();
        try {
            JSONArray savedContraptions = new JSONArray(new JSONTokener(new FileReader(file)));
            for (int i = 0; i < savedContraptions.length(); i++) {
                JSONObject savedContraption = savedContraptions.getJSONObject(i);
                String ID = savedContraption.getString("ID");
                ContraptionsPlugin.toConsole("Loading Factory with ID " + ID);
                if (contraptionProperties.containsKey(ID)) {
                    Contraption contraption = contraptionProperties.get(ID).loadContraption(savedContraption);
                    //If there isn't already a Contraption at the location load up the contraption
                    if (!contraptions.containsKey(contraption.getLocation())) {
                        contraptions.put(contraption.getLocation(), contraption);
                        ContraptionsPlugin.toConsole("Loaded Factory: " + contraption.save().toString(2));
                    }
                    //If there is already a Contraption at that location permenantly delete that contraption
                    //However log the information for larborous fixing that should never need to be done
                    else {
                        ContraptionsPlugin.toConsole("Deleting Factory due to location conflict:\n" + contraption.save().toString(2));
                    }
                } else {
                    lostContraptions.add(savedContraption);
                    ContraptionsPlugin.toConsole("Factory ID not found. ID = " + ID);
                }

            }
        } catch (FileNotFoundException e) {
            ContraptionsPlugin.toConsole("Failed to load contraption save file: " + file.getName());
            e.printStackTrace();
        }
        return lostContraptions;
    }

    /**
     * Saves all the current Contraptions to a file
     *
     * @param file File Contraptions are saved to
     */
    public void saveContraptions(File file) {
        try {
            ContraptionsPlugin.toConsole("Saving Contraptions...");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            JSONWriter jsonWriter = new JSONWriter(bufferedWriter);
            jsonWriter.array();
            for (Contraption contraption : contraptions.values()) {
                jsonWriter.value(contraption.save());
            }
            jsonWriter.endArray();
            bufferedWriter.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a Contraption associated with the given location
     *
     * @param location The location to check
     * @return Contraption at location or null
     */
    public Contraption getContraption(Location location) {
        return contraptions.get(location);

    }

    /**
     * Gets contraptions located within a square around the given location
     *
     * @param location Central location
     * @param radius   Square radius from which to search
     * @return Set of contraptions in radius
     */
    public Set<Contraption> getContraptions(Location location, int radius) {
        Set<Contraption> contraptions = new HashSet<Contraption>();
        Contraption contraption;
        Location currentLocation = location.clone();

        for (int x = -radius; x <= radius; x++) {
            currentLocation.setX(x);
            for (int z = -radius; z <= radius; z++) {
                currentLocation.setZ(z);
                contraption = getContraption(currentLocation);
                if (contraption != null) {
                    contraptions.add(contraption);
                }
            }
        }
        return contraptions;
    }

    /**
     * Creates a factory at the location
     *
     * @param location The location to create a contraption
     * @return The response related to the creation solution
     */
    public Response createContraption(Location location, Player player) {
        Block block = location.getBlock();
        boolean matchedBlock = false;
        Response response;
        for (ContraptionProperties contraptionProperty : contraptionProperties.values()) {
            if (contraptionProperty.validBlock(block)) {
                matchedBlock = true;
                response = contraptionProperty.createContraption(location);
                if (response.getSuccess()) {
                    //Publish creation to console for logging
                    StringBuilder alert = new StringBuilder();
                    alert.append("Created ").append(response.getContraption().getName());
                    alert.append(" belonging to group ").append(response.getContraption().getGroup());
                    alert.append(" at (").append(location.getBlockX()).append(" ").append(location.getBlockY()).append(" ").append(location.getBlockZ());
                    alert.append(") by player ").append(player.getUniqueId());
                    ContraptionsPlugin.toConsole(alert.toString());

                    return response;
                }
            }
        }
        if (matchedBlock) {
            return new Response(false, "Block is correct, but other conditions are not");
        } else {
            return new Response(false, "Incorrect block");
        }

    }

    /**
     * Registers a contraption with this manager
     *
     * @param cotraption Contraption to register
     */
    public void registerContraption(Contraption cotraption) {
        contraptions.put(cotraption.getLocation(), cotraption);
    }

    /**
     * Completely eliminates the Contraption
     *
     * @param contraption Contraption to be destroyed
     */
    public void destroy(Contraption contraption) {
        Location location = contraption.getLocation();
        if (contraptions.containsKey(location)) {
            contraptions.remove(location);
            String eventMessage = String.format("Destroyed %s at (%d, %d, %d)", contraption.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
            ContraptionsPlugin.toConsole(eventMessage);
            contraption.destroy();
        }
    }

    /**
     * Handles blocks being broken
     *
     * @param block Block which was broken
     */
    public void handleBlockDestruction(Block block) {
        if (contraptions.containsKey(block.getLocation())) {
            destroy(contraptions.get(block.getLocation()));
        }
    }

    /**
     * Responds to player interaction events
     *
     * @param e The PlayerInteractionEvent
     */
    public void handleInteraction(PlayerInteractEvent e) {
        //if the player left clicked a block
        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        Player player = e.getPlayer();
        //If the player was holding a item matching the interaction material
        if (!player.getItemInHand().getType().equals(Material.STICK)) {
            return;
        }
        Location location = e.getClickedBlock().getLocation();
        //Checks if permissions are active and block is reinforced
        if (ContraptionsPlugin.PERMISSIONS && reinforcementManager.isReinforced(location)) {
            if (((PlayerReinforcement) reinforcementManager.getReinforcement(location)).isAccessible(player, PermissionType.CHESTS)) {
                Response response = new Response(false, "You don't have permission to interact with Contraptions at this block");
                response.conveyTo(player);
                return;
            }
        }
        Contraption contraption = getContraption(location);
        //If a contraption doesn't exist on location
        if (contraption == null) {
            createContraption(location, player).conveyTo(player);
        } //If contraption exists at location
        else {
            contraption.trigger().conveyTo(player);
        }

    }

    /**
     * Gets the reinforcement manager
     *
     * @return The ReinforcementManager
     */
    public ReinforcementManager getReinforcementManager() {
        return reinforcementManager;
    }
}
