package vg.civcraft.mc.contraptions.utility;

import org.bukkit.Location;
import org.bukkit.Sound;

public enum SoundType {
    CREATION(Sound.FIREWORK_BLAST, Sound.BURP, Sound.IRONGOLEM_DEATH),
    PRODUCTION(Sound.ITEM_BREAK,Sound.CLICK,Sound.PISTON_EXTEND,Sound.WOLF_SHAKE),
    DESTRUCTION(Sound.SKELETON_DEATH,Sound.SLIME_WALK,Sound.SILVERFISH_KILL);
    
    Sound[] sounds;
    SoundType(Sound... sounds) {
     this.sounds = sounds;
    }

    public void play(BlockLocation location) {
        for (Sound sound : sounds) {
            location.getWorld().playSound(location.getBukkitLocation(), sound, 1f, 1f);
        }
    }
}
