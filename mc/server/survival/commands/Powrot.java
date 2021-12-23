package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ServerUtil;
import mc.server.survival.utils.VisualUtil;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Powrot 
implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			
			if (args.length == 0)
				if (DataManager.getInstance().getLocal(player).getLastLocation().equals(WorldUtil.SURVIVAL_SPAWN))
					ChatManager.sendNotification(player, "Miejsce, na ktore probujesz powrocic zostalo juz przez Ciebie uzyte lub odleglosc do niego jest zbyt mala!", ChatManager.NotificationType.ERROR);
				else
				{
					player.teleport(DataManager.getInstance().getLocal(player).getLastLocation());

					new BukkitRunnable() {
						@Override
						public void run() {
							player.closeInventory();
							VisualUtil.showServerChangeTitle(player);
							ServerUtil.reloadContents(player);
						}
					}.runTaskLater(Main.getInstance(), 20L);

					ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja do ostatniej lokacji... #fff203⌛");
					DataManager.getInstance().getLocal(player).resetLastLocation();
				}
			else
				ChatManager.sendNotification(player, "Umiesz pisac?! Wystarczy napisac #ffc936/powrot!", ChatManager.NotificationType.ERROR);

			return true;
		}
		
		return false;
	}
}