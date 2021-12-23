package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace
implements Listener
{
    @EventHandler
    public void onEvent(BlockPlaceEvent event)
    {
        final Player player = event.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("survival") && Configuration.SERVER_TERRAIN_PROTECTION)
            if (allowedDist(event.getBlock().getLocation(), WorldUtil.SURVIVAL_SPAWN))
                event.setCancelled(true);
    }

    private boolean allowedDist(Location from, Location to)
    {
        return from.distance(to) <= Configuration.SERVER_SPAWN_PROTECTION;
    }
}