package vg.civcraft.mc.contraptions.utility;

import vg.civcraft.mc.contraptions.contraptions.Contraption;
import org.bukkit.entity.Player;
import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.Menu;

/**
 * Conveys a response to a triggered event
 */
public class Response {

    boolean success;
    Menu message;
    Contraption contraption;

    /**
     * Creates the Response
     *
     * @param success Whether the action was successful
     * @param message A message associated with the action
     */
    public Response(boolean success, String message) {
        this.success = success;
        this.message = CivMenu.newMenu(message);
    }

    /**
     * Creates the Response
     *
     * @param success Whether the action was successful
     * @param message A message associated with the action
     */
    public Response(boolean success, Menu message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a Response
     *
     * @param success     Whether the action was successful
     * @param message     A message associated with the action
     * @param contraption Associated contraption
     */
    public Response(boolean success, String message, Contraption contraption) {
        this(success, message);
        this.contraption = contraption;
    }

    /**
     * Creates a Response
     *
     * @param success     Whether the action was successful
     * @param message     A message associated with the action
     * @param contraption Associated contraption
     */
    public Response(boolean success, Menu message, Contraption contraption) {
        this(success, message);
        this.contraption = contraption;
    }

    /**
     * Gets if the response was successful
     *
     * @return If the response was successful
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Checks if response has an associated contraption
     *
     * @return If Response has a associated Contraption
     */
    public boolean hasContraption() {
        return contraption != null;
    }

    /**
     * Gets the Contraption associated with the response
     *
     * @return The Contraption associated with the response, null if there is
     *         none
     */
    public Contraption getContraption() {
        return contraption;
    }

    /**
     * Messages the player about the response
     *
     * @param player The player to massage
     */
    public void conveyTo(Player player) {
        message.send(player);
    }

}
