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

public class Dom
implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			
			if (args.length == 0)
			{
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa otwieranie menu Twoich domkow... #fff203⌛");
				new BukkitRunnable() { @Override public void run() {
					InventoryUtil.createNewInventory(player, 27, ChatColor.translateAlternateColorCodes('&', "&c&lDOMKI"), "domki");
				} }.runTaskLater(Main.getInstance(), 20L);
			}
			else
				ChatManager.sendNotification(player, "Umiesz pisac?! Wystarczy napisac #ffc936/dom!", ChatManager.NotificationType.ERROR);

			return true;
		}
		
		return false;
	}
}