package mc.server.survival.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtil
{
    public static void playPlayerSound(final Player player, final Sound sound, final int pitch, final int volume)
    {
        player.playSound(player.getLocation(), sound, pitch, volume);
    }
}