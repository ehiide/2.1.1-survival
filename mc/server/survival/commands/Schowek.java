package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Schowek
implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final int length = args.length;
			
			if (length == 0)
			{
				InventoryUtil.createSchowek(player);
				return true;
			}
			else if (length == 1 && args[0].equalsIgnoreCase("ulepsz") | args[0].equalsIgnoreCase("ulepszenie") | args[0].equalsIgnoreCase("upgrade"))
			{
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa otwieranie menu ulepszen schowku... #fff203⌛");
				new BukkitRunnable() { @Override public void run() {
					InventoryUtil.createNewInventory(player, 27, ChatColor.translateAlternateColorCodes('&', "&c&lULEPSZENIE SCHOWKU"), "schowek");
				} }.runTaskLater(Main.getInstance(), 20L);
				return true;
			}
			else
			{
				ChatManager.sendNotification(player, "Sluchaj no, " + player.getName() + "... poprawne wpisanie komendy to podstawa!", ChatManager.NotificationType.ERROR);
				return true;
			}
		}
		
		return false;
	}
}