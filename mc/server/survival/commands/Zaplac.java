package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.MathUtil;
import mc.server.survival.utils.ServerUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Zaplac
implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final int length = args.length;
			
			if (length >= 3)
			{
				ChatManager.sendNotification(player, "Co Ty tworzysz! Wzor komendy: #ffc936/zaplac <gracz> <ilosc>!", ChatManager.NotificationType.ERROR);
				return true;
			}
			
			if (length == 0)
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cSponsor, widze! Sluchaj... podaj nazwe gracza, ktoremu chcesz zaplacic!");
			else
			{
				final String potencial_player = args[0];
				
				for (Player dplayer : Bukkit.getOnlinePlayers())
					if (dplayer.getName().equalsIgnoreCase(potencial_player)) 
					{	
						if (length == 1)
							ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz sume monet jaka chcesz przelac graczowi " + ChatUtil.returnPlayerColor(player) + dplayer.getName() + "!");
						else
							{
							if (MathUtil.isInteger(args[1]))
							{
								final int check = Integer.parseInt(args[1]);
								
								if (check > DataManager.getInstance().getLocal(player).getMoney())
								{
									ChatManager.sendNotification(player, "Nie posiadasz nawet tyle w portfelu!", ChatManager.NotificationType.ERROR);
									return true;
								}

								DataManager.getInstance().getLocal(player).removeMoney(check);
								DataManager.getInstance().getLocal(dplayer).addMoney(check);
								ChatManager.sendMessage(player, "#f83044&l» &cPrzelano kwote &e" + check + ",- &emonet &cdo " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + "!");
								ChatManager.sendMessage(dplayer, "#f83044&l» &cOtrzymano kwote &e" + check + ",- &emonet &cod " + ChatUtil.returnPlayerColor(player) + player.getName() + "!");
								VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#ffc936⛃", 0, 10, 10);
								ServerUtil.reloadContents(player);
								ServerUtil.reloadContents(dplayer);
								return true;
							}

							ChatManager.sendNotification(player, "Ta kwota to liczba?! Wzor komendy: #ffc936/zaplac <gracz> <ilosc>!", ChatManager.NotificationType.ERROR);
						}

						return true;
					}

				ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
			}

			return true;
		}
		
		return false;
	}
}