package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Wiadomosc implements CommandExecutor
{
	private static HashMap<Player, Player> reply_queue = new HashMap<>();
	protected static Player getReply(Player player)
	{
		return reply_queue.get(player);
	}
	protected static void setReply(Player player, Player address)
	{
		reply_queue.put(player, address);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final int length = args.length;
			
			if (length == 0)
			{
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz nazwe gracza! Wzor komendy: #ffc936/wiadomosc <gracz> <wiadomosc>");
				return true;
			}
			else
			{
				final String potencial_player = args[0];
				
				for (Player dplayer : Bukkit.getOnlinePlayers())
					if (dplayer.getName().equalsIgnoreCase(potencial_player))
						if (length == 1)
						{
							ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz swoja wiadomosc! Wzor komendy: #ffc936/wiadomosc <gracz> <wiadomosc>");
							return true;
						}
						else
						{
							StringBuilder message = new StringBuilder();

							for (int word = 1; word < length; word++)
								if (word + 1 >= length)
									message.append(args[word].toLowerCase());
								else
									message.append(args[word].toLowerCase()).append(" ");

							ChatManager.sendMessage(player, "#f83044&l» &cDo " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + ": &r&7" + ChatUtil.applyCorrection(ChatUtil.getPlaceholder(player, message.toString())));
							ChatManager.sendMessage(dplayer, "#f83044&l» &cWiadomosc od " + ChatUtil.returnPlayerColor(player) + player.getName() + ": &r&7" + ChatUtil.applyCorrection(ChatUtil.getPlaceholder(player, message.toString())));
							VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#faff26✉", 0, 10, 10);

							setReply(dplayer, player);
							setReply(player, dplayer);

							return true;
						}
				
				if (!Bukkit.getOfflinePlayer(potencial_player).isOnline()) 
				{
					ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
					return true;
				}
			}
		}
		
		return false;
	}
}