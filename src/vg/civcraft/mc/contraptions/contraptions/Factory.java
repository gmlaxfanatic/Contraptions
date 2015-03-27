package vg.civcraft.mc.contraptions.contraptions;

import vg.civcraft.mc.contraptions.gadgets.ProductionGadget;
import vg.civcraft.mc.contraptions.utility.Resource;
import vg.civcraft.mc.contraptions.properties.FactoryProperties;
import vg.civcraft.mc.contraptions.utility.Response;
import vg.civcraft.mc.contraptions.utility.SoundType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.Menu;
import vg.civcraft.mc.civmenu.MenuCommand;
import vg.civcraft.mc.contraptions.utility.Anchor;

/**
 * A Factory Contraption meant to replace the current ProductionFactory object
 * in FactoryMod.
 *
 * It is created by placing a specific set of Items in a chest and tapping it
 * with a stick. It then will start to lose energy slowly and requires fuel
 * items to be placed in the chest to re-energize it. The factory allows the
 * execution of a single recipe by placing the raw ingrediants in the chest and
 * then hitting the chest with a stick, which will allow the final produce to be
 * made
 */
public class Factory extends Contraption implements MenuCommand {

    static String ENERGY_KEY = "Energy";
    Resource energy;

    /**
     * Creates a Factory Contraption
     *
     * @param properties The Factory Properties Object
     * @param Anchor   The Anchor of the Contraption
     */
    public Factory(FactoryProperties properties, Anchor anchor) {
        super(properties, anchor);
        energy = new Resource(0, this);
        tasks.add(properties.getGrowGadget().run(energy));
    }

    @Override
    public JSONObject getResources() {
        JSONObject resources = new JSONObject();
        resources.put(ENERGY_KEY, energy.get());
        return resources;
    }

    @Override
    public void loadResources(JSONObject jsonObject) {
        energy = new Resource(jsonObject.getDouble(ENERGY_KEY), this);
    }

    @Override
    public Response trigger() {
        /*
         * Create a message with the following format 1 You are using a %name.
         * Click on a recipe to use it 2 Recipe1 Recipe2 ... Repair
         */
        Menu menu = CivMenu.newMenu();
        menu.addEntry("You are using a " + properties.getName() + ". Click on a recipe to use it:\n");
        int index = 0;
        for (ProductionGadget gadget : getProperties().getProductionGadgets()) {
            menu.addEntry(gadget.getName())
                    .setHover(new Object[]{"Convert: ", gadget.getInputs(), "\nTo: ", gadget.getOutputs()})
                    .setCommand(menu,new String[]{Integer.toString(index)});
            index++;
        }
        menu.addEntry("Repair")
                .setHover("Repair this Factory");
        return new Response(true, menu, this);
    }

    public boolean execute(CommandSender sender, String args[]) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }
        Player player = (Player) sender;
        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        Menu menu = CivMenu.newMenu();
        ProductionGadget gadget = getProperties().getProductionGadgets().get(index);
        if (gadget.produceGoods(getInventory())) {
            SoundType.PRODUCTION.play(anchor.getBukkitLocation());
            menu.addEntry("Produced ", gadget.getOutputs());
            menu.send(player);
            return true;
        }
        menu.addEntry("Cannot Produce ", gadget.getOutputs());
        menu.send(player);
        return false;
    }

    @Override
    protected FactoryProperties getProperties() {
        return (FactoryProperties) properties;
    }

    @Override
    public void update(Resource resource) {
        //If a change in energy triggered this update, check that energy is good
        if (resource == energy) {
            getProperties().getMinMaxGadget().update(resource);
            //If contraption ran out of energy destroy it
            if (energy.get() < 0) {
                getContraptionManager().destroy(this);
            }
        }
        update();
    }

    @Override
    public boolean hasResource(String resourceID) {
        return resourceID.equals(ENERGY_KEY);
    }

    @Override
    public Resource getResource(String resourceID) {
        return resourceID.equals(ENERGY_KEY) ? energy : null;
    }

}
