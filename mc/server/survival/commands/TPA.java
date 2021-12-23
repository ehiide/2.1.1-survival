package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TPA
implements CommandExecutor
{
	private static HashMap<Player, Player> tpa_queue = new HashMap<>();
	private static Player getTPA(Player player)
	{
		return tpa_queue.get(player);
	}
	private static void setTPA(Player player, Player address)
	{
		tpa_queue.put(player, address);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
  		{
			final Player player = (Player) sender;
			final int length = args.length;

			if (length == 0)
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza! Wzor komendy: #ffc936/tpa <gracz>");
			else
			{
				if (length >= 3)
				{
					ChatManager.sendNotification(player, "Spokojnie, spokojnie, jeden gracz wystarczy! Wzor komendy: #ffc936/tpa <gracz>", ChatManager.NotificationType.ERROR);
					return true;
				}

				if (args[0].equalsIgnoreCase("akceptuj"))
				{
					if (length == 1)
					{
						ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, od ktorego otrzymales prosbe teleportacji! Wzor komendy: #ffc936/tpa akceptuj <gracz>");
						return true;
					}

					final String potencial_player = args[1];

					for (Player dplayer : Bukkit.getOnlinePlayers())
						if (dplayer.getName().equalsIgnoreCase(potencial_player))
						{
							if (getTPA(dplayer) == player)
							{
								ChatManager.sendMessage(player, "&c&l» &fProsba teleportacji od gracza " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + " &fzostala zaakceptowana!");
								ChatManager.sendMessage(dplayer, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja do gracza " + ChatUtil.returnPlayerColor(player) + player.getName() + "... #fff203⌛");
								setTPA(dplayer, null);
								dplayer.teleport(player);
								return true;
							}

							ChatManager.sendNotification(player, "Wy mieliscie sie tepnac? Moze czas minal, moze gracz wyslal juz prosbe komus innemu, ja nic nie wiem!", ChatManager.NotificationType.ERROR);
							return true;
						}

					ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
					return true;
				}
				else if (args[0].equalsIgnoreCase("odrzuc"))
				{
					if (length == 1)
					{
						ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, od ktorego otrzymales prosbe teleportacji! Wzor komendy: #ffc936/tpa odrzuc <gracz>");
						return true;
					}

					final String potencial_player = args[1];

					for (Player dplayer : Bukkit.getOnlinePlayers())
						if (dplayer.getName().equalsIgnoreCase(potencial_player))
						{
							if (getTPA(dplayer)== player)
							{
								ChatManager.sendMessage(dplayer, "#f83044&l» #fc7474Prosba teleportacji do gracza " + ChatUtil.returnPlayerColor(player) + player.getName() + " #fc7474zostala odrzucona!");
								ChatManager.sendMessage(player, "#f83044&l» &cProsba teleportacji od gracza " + ChatUtil.returnPlayerColor(player) + dplayer.getName() + " &czostala odrzucona!");
								setTPA(dplayer, null);
								return true;
							}

							ChatManager.sendNotification(player, "Wy mieliscie sie tepnac? Moze czas minal, moze gracz wyslal juz prosbe komus innemu, ja nic nie wiem!", ChatManager.NotificationType.ERROR);
							return true;
						}

					ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
					return true;
				}

				final String potencial_player = args[0];

				for (Player dplayer : Bukkit.getOnlinePlayers())
				{
					if (dplayer.getName().equalsIgnoreCase(potencial_player))
					{
						setTPA(player, null);
						setTPA(player, dplayer);
						ChatManager.sendMessage(player, "#f83044&l» &cWyslano prosbe teleportacji do gracza " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + "!");
						ChatManager.sendMessage(dplayer, "#f83044&l» &cOtrzymano prosbe teleportacji od gracza " + ChatUtil.returnPlayerColor(player) + player.getName() + "!\n#fc7474&o/tpa akceptuj/odrzuc (gracz)&f&o - zaakceptowanie/odrzucenie\n#fc7474&o" + Configuration.SERVER_TELEPORT_REQUEST_TIME + " sekund&f&o - czas oczekiwania");
						VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#80ff1f⌛", 0, 20, 20);

						new BukkitRunnable() { @Override public void run() {
							setTPA(player, null);
						} }.runTaskLaterAsynchronously(Main.getInstance(), 20*Configuration.SERVER_TELEPORT_REQUEST_TIME);
						return true;
					}
				}
				ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
			}

			return true;
		}
		
		return false;
	}
}