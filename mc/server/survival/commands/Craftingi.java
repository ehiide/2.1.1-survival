package mc.server.survival.commands;

import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Craftingi
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
				InventoryUtil.createNewInventory(player, 45, ChatColor.translateAlternateColorCodes('&', "&c&lCRAFTINGI (1/7)"), "craftingi1");
			else
			{
				if (length <= 1)
				{
					final String page = args[0];

					if (page.equalsIgnoreCase("1") || page.equalsIgnoreCase("2") ||
							page.equalsIgnoreCase("3") || page.equalsIgnoreCase("4") ||
							page.equalsIgnoreCase("5") || page.equalsIgnoreCase("6") ||
							page.equalsIgnoreCase("7"))
					{
						InventoryUtil.createNewInventory(player, 45, ChatColor.translateAlternateColorCodes('&', "&c&lCRAFTINGI (" + page + "/7)"), "craftingi" + page);
						return true;
					}
					else
						ChatManager.sendNotification(player, "Koledze sie chyba strony pojebaly! Mozesz wybrac strone #ffc9361-7!", ChatManager.NotificationType.ERROR);
				}
				else
					ChatManager.sendNotification(player, "Umiesz pisac?! Wystarczy napisac #ffc936/craftingi (strona)!", ChatManager.NotificationType.ERROR);
			}

			return true;
		}
		
		return false;
	}
}