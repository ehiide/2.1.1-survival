package mc.server.survival.commands;

import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Sklep
implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			
			if (args.length == 0)
				InventoryUtil.createNewInventory(player, 45, ChatColor.translateAlternateColorCodes('&', "&c&lSKLEP"), "sklep");
			else
				ChatManager.sendNotification(player, "Umiesz pisac?! Wystarczy napisac #ffc936/sklep!", ChatManager.NotificationType.ERROR);

			return true;
		}
		
		return false;
	}
}