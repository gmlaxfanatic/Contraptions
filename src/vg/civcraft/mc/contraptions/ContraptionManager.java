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
import java.util.Iterator;
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
import vg.civcraft.mc.contraptions.utility.BlockLocation;
import vg.civcraft.mc.contraptions.utility.DAO;
import vg.civcraft.mc.namelayer.permission.PermissionType;

/**
 * Manages access, loading, and saving of Contraptions
 */
public class ContraptionManager {

    Plugin plugin;
    Map<String, ContraptionProperties> contraptionProperties;
    ReinforcementManager reinforcementManager;
    DAO dao;

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
        dao = new DAO();
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
                    //If there isn't already a Contraption at the bukkitLocation load up the contraption
                    if (dao.getContraption(contraption.getLocation())!=null) {
                        dao.registerContraption(contraption);
                        ContraptionsPlugin.toConsole("Loaded Factory: " + contraption.save().toString(2));
                    }
                    //If there is already a Contraption at that bukkitLocation permenantly delete that contraption
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
            Iterator<Contraption> contraptions = dao.iterator();
            while(contraptions.hasNext()){
                jsonWriter.value(contraptions.next().save());
            }
            jsonWriter.endArray();
            bufferedWriter.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a Contraption associated with the given bukkitLocation
     *
     * @param location The bukkitLocation to check
     * @return Contraption at bukkitLocation or null
     */
    public Contraption getContraption(BlockLocation location) {
        return dao.getContraption(location);

    }

    /**
     * Gets contraptions located within a square around the given bukkitLocation
     *
     * @param location Central bukkitLocation
     * @param radius   Square radius from which to search
     * @return Set of contraptions in radius
     */
    public Set<Contraption> getContraptions(BlockLocation location, int radius) {
        Set<Contraption> contraptions = new HashSet<Contraption>();
        Contraption contraption;
        for (int x = -radius; x <= radius; x++) {
           for (int z = -radius; z <= radius; z++) {
                contraption = getContraption(new BlockLocation(location.world,x,location.y,z));
                if (contraption != null) {
                    contraptions.add(contraption);
                }
            }
        }
        return contraptions;
    }

    /**
     * Creates a factory at the bukkitLocation
     *
     * @param location The bukkitLocation to create a contraption
     * @return The response related to the creation solution
     */
    public Response createContraption(BlockLocation location, Player player) {
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
                    alert.append(" at (").append(location.x).append(" ").append(location.y).append(" ").append(location.z);
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
        dao.registerContraption(cotraption);
        dao.registerAssociatedBlocks(cotraption);
    }

    /**
     * Completely eliminates the Contraption
     *
     * @param contraption Contraption to be destroyed
     */
    public void destroy(Contraption contraption) {
        if (dao.removeContraption(contraption)) {
            dao.removeAssociatedBlocks(contraption);
            contraption.destroy();
        }
    }

    /**
     * Handles blocks being broken
     *
     * @param block Block which was broken
     */
    public void handleBlockDestruction(Block block) {
        for(Contraption contraption:dao.getContraptions(new BlockLocation(block.getLocation()))){
            contraption.blockDestroyed(block);
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
        Location bukkitLocation = e.getClickedBlock().getLocation();
        //Checks if permissions are active and block is reinforced
        if (ContraptionsPlugin.PERMISSIONS && reinforcementManager.isReinforced(bukkitLocation)) {
            if (((PlayerReinforcement) reinforcementManager.getReinforcement(bukkitLocation)).isAccessible(player, PermissionType.CHESTS)) {
                Response response = new Response(false, "You don't have permission to interact with Contraptions at this block");
                response.conveyTo(player);
                return;
            }
        }
        BlockLocation location = new BlockLocation(bukkitLocation);
        Contraption contraption = dao.getContraption(location);
        //If a contraption doesn't exist on bukkitLocation
        if (contraption == null) {
            createContraption(location, player).conveyTo(player);
        } //If contraption exists at bukkitLocation
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
