package com.untamedears.contraptions.utility;

import com.untamedears.contraptions.contraptions.Contraption;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Conveys a response to a triggered event
 */
public class Response {

    boolean success;
    String message;
    Contraption contraption;

    /**
     * Creates the Response
     *
     * @param success Whether the action was successful
     * @param message A message associated with the action
     */
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a Response
     *
     * @param success Whether the action was successful
     * @param message A message associated with the action
     * @param contraption Associated contraption
     */
    public Response(boolean success, String message, Contraption contraption) {
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
     * Gets the message associated with the response
     *
     * @return The message associated with the response
     */
    public String getMessage() {
        return message;
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
     * none
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
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(message)));
    }

}
