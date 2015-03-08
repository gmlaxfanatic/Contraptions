package com.untamedears.contraptions;

import com.untamedears.contraptions.contraptions.Contraption;
import com.untamedears.contraptions.properties.ContraptionProperties;
import com.untamedears.contraptions.properties.FactoryProperties;
import com.untamedears.contraptions.utlity.Response;
import java.io.BufferedWriter;
import java.io.File;
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

public class ContraptionManager {

    Plugin plugin;
    Map<String, ContraptionProperties> contraptionProperties;
    Map<Location, Contraption> contraptions;

    public ContraptionManager(Plugin plugin) {
        this.plugin = plugin;
        //There has gotta be a better way to do this
    }

    public void onEnable() {
        try {
            contraptionProperties = loadProperties(new File(plugin.getDataFolder(), "config.json"));
        } catch (Exception e) {
            throw e;
        }
        try {
            contraptions = loadContraptions(new File(plugin.getDataFolder(), "savefile.json"));
        } catch (Exception e) {
            e.printStackTrace();
            contraptions = new HashMap<Location, Contraption>();
        }
    }

    public void onDisable() {

    }

    /**
     * Loads the ContraptionProperties from the Config File
     * <p>
     * @param contraptionProperties
     */
    private Map<String, ContraptionProperties> loadProperties(File file) {
        Map<String, ContraptionProperties> newContraptionProperties = new HashMap<String, ContraptionProperties>();
        try {
            JSONTokener tokener = new JSONTokener(new FileReader(file));
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject factories = jsonObject.getJSONObject("Factory");
            for (String ID : factories.keySet()) {
                newContraptionProperties.put(ID, FactoryProperties.fromConfig(this, ID, factories.getJSONObject(ID)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newContraptionProperties;
    }

    /**
     * Loads the Contraptions from a file
     * <p>
     * Contraptions should be formatted as followg:
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
     * <p>
     * @param file File to be loaded
     * @return New Map of Contraptions by Location
     */
    public Map<Location, Contraption> loadContraptions(File file) {
        Map<Location, Contraption> newContraptions = new HashMap<Location, Contraption>();
        try {
            JSONTokener tokener = new JSONTokener(new FileReader(file));
            JSONArray savedContraptions = new JSONArray(tokener);
            for (int i = 0; i < savedContraptions.length(); i++) {
                JSONObject savedContraption = savedContraptions.getJSONObject(i);
                String ID = savedContraption.getString("ID");
                Contraption contraption = contraptionProperties.get(ID).loadContraption(savedContraption);
                newContraptions.put(contraption.getLocation(), contraption);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newContraptions;
    }

    /**
     * Saves all the current Contraptions to a file
     * <p>
     * @param file File Contraptions are saved to
     */
    public void saveContraptions(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            JSONWriter jsonWriter = new JSONWriter(bufferedWriter);
            jsonWriter.array();
            for(Contraption contraption:contraptions.values()) {
                jsonWriter.value(contraption.save());
            }
            jsonWriter.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a Contraption associated with the given location
     * <p>
     * @param location The location to check
     * @return Contraption at location or null
     */
    public Contraption getContraption(Location location) {
        return contraptions.get(location);

    }

    /**
     * Gets contraptions located within a square around the given location
     * <p>
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
     * <p>
     * @param location The location to create a contraption
     * @return The response related to the creation solution
     */
    public Response createContraption(Location location) {
        Block block = location.getBlock();
        boolean matchedBlock = false;
        Response response;
        for (ContraptionProperties contraptionProperty : contraptionProperties.values()) {
            if (contraptionProperty.validBlock(block)) {
                matchedBlock = true;
                response = contraptionProperty.createContraption(location);
                if (response.success) {
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
     * Regesiters a contraption with this manager
     * <p>
     * @param cotraption Contraption to register
     */
    public void registerContraption(Contraption cotraption) {
        contraptions.put(cotraption.getLocation(), cotraption);
    }

    /**
     * Completely eliminates the Contraption
     * <p>
     * @param contraption Contraption to be destroyed
     */
    public void destroy(Contraption contraption) {
        if (contraptions.containsKey(contraption.getLocation())) {
            contraptions.remove(contraption.getLocation());
            contraption.destroy();
        }
    }

    /**
     * Handles blocks being broken
     * <p>
     * @param block Block which was broken
     */
    public void handleBlockDestruction(Block block) {
        if (contraptions.containsKey(block.getLocation())) {
            destroy(contraptions.get(block.getLocation()));
        }
    }

    /**
     * Responds to player interaction events
     * <p>
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

        Contraption contraption = getContraption(location);
        //If a contraption doesn't exist on location
        if (contraption == null) {
            createContraption(location).conveyTo(player);
        } //If contraption exists at location
        else {
            contraption.trigger().conveyTo(player);
        }

    }
}
