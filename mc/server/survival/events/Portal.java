package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Portal 
implements Listener
{
	@EventHandler
	public void onEvent(PlayerPortalEvent event)
	{
		final Player player = event.getPlayer();
		final World world = event.getFrom().getWorld();
		final World sworld = event.getTo().getWorld();
		final String worldName = world.getName();
		final String sworldName = sworld.getName();

		PlayerTeleportEvent.TeleportCause cause = event.getCause();

		if (worldName.equalsIgnoreCase("surowce"))
		{
			ChatManager.sendNotification(player, "Nie mozesz uzywac systemow teleportacji na tym swiecie!", ChatManager.NotificationType.ERROR);
			event.setCancelled(true);
			return;
		}

		if (cause.equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL))
		{
			if (Configuration.SERVER_BLOCK_NETHER)
				event.setCancelled(true);

			if (sworldName.equalsIgnoreCase("survival_nether"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.DIMENSIONABLE);

			if (sworldName.equalsIgnoreCase("survival"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.SURVIVAL);
		}

		if (cause.equals(PlayerTeleportEvent.TeleportCause.END_PORTAL))
		{
			if (Configuration.SERVER_BLOCK_THE_END)
				event.setCancelled(true);

			if (sworldName.equalsIgnoreCase("survival_the_end"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.DIMENSIONABLE);

			if (sworldName.equalsIgnoreCase("survival"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.SURVIVAL);
		}
	}
}