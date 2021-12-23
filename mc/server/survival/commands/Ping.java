package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Ping 
implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;

			if (args.length == 0)
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTwoj ping wynosi: #fff203" + ServerUtil.getPing(player) + " #8c8c8cms!");
			else
			{
				final String potencial_player = args[0];
				
				for (Player dplayer : Bukkit.getOnlinePlayers())
					if (dplayer.getName().equalsIgnoreCase(potencial_player)) 
					{	
						ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cPing " + ChatUtil.returnPlayerColor(player) + dplayer.getName() + "#8c8c8c wynosi: #fff203" + ServerUtil.getPing(dplayer) + " #8c8c8cms!");
						return true;
					}

				ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
			}
			return true;
		}
		
		return false;
	}
}