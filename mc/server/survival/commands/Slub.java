package mc.server.survival.commands;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Slub
implements CommandExecutor
{
	private static HashMap<Player, Player> slub_queue = new HashMap<>();
	private static Player getMarry(Player player)
	{
		return slub_queue.get(player);
	}
	private static void setMarry(Player player, Player address)
	{
		slub_queue.put(player, address);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final int length = args.length;
			
			if (length == 0)
				ChatManager.sendMessages(player, "", "#f83044&m---------------------------------------",
						"#fc7474/slub (gracz) #8c8c8c- wysyla zareczyny do konkretnego gracza",
						"#fc7474/slub akceptuj (gracz) #8c8c8c- akceptuje zareczyny wyslane przez konkretnego gracza",
						"#fc7474/slub odrzuc (gracz) #8c8c8c- odrzuca zareczyny wyslane przez konkretnego gracza",
						"#fc7474/slub status [gracz] #8c8c8c- pozwala sprawdzac z kim jest w zwiazku dany gracz",
						"#fc7474/slub poziom #8c8c8c- pokazuje szczegoly dzialania systemu poziomow slubu",
						"&8&o(argumenty wymagane - (), argumewnty opcjonalne - [])",
						"#f83044&m---------------------------------------", "");
			else
			{
				if (!player.getWorld().getName().equalsIgnoreCase("survival"))
				{
					ChatManager.sendNotification(player, "Kazdy slub powinien byc uroczysty, zadbaj o to, biorac slub w kosciele, na lobby serwera!", ChatManager.NotificationType.ERROR);
					return true;
				}
				if (player.getLocation().distance(WorldUtil.SURVIVAL_CHURCH) > 20)
				{
					ChatManager.sendNotification(player, "Formalnosci ponad wszystko, najpierw udaj sie do kosciola!", ChatManager.NotificationType.ERROR);
					return true;
				}

				if (length >= 3)
				{
					ChatManager.sendNotification(player, "Spokojnie, spokojnie, wpisz komende jeszcze raz, nie mozemy nadazyc za toba!", ChatManager.NotificationType.ERROR);
					return true;
				}

				if (args[0].equalsIgnoreCase("akceptuj"))
				{
					if (length == 1)
					{
						ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, od ktorego otrzymales zareczyny! Wzor komendy: #ffc936/slub akceptuj <gracz>");
						return true;
					}

					if (getMarry(player) != null)
					{
						ChatManager.sendNotification(player, "Nie zdradzaj swojego partnera(ki), " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(player).getMarry()) + DataManager.getInstance().getLocal(player).getMarry() + "!", ChatManager.NotificationType.ERROR);
						return true;
					}

					final String potencial_player = args[1];

					for (Player dplayer : Bukkit.getOnlinePlayers())
						if (dplayer.getName().equalsIgnoreCase(potencial_player))
						{
							if (getMarry(dplayer) == player)
							{
								setMarry(player,null);
								setMarry(dplayer,null);

								DataManager.getInstance().getLocal(dplayer).setMarryLevel(0);
								DataManager.getInstance().getLocal(player).setMarryLevel(0);

								DataManager.getInstance().getLocal(dplayer).setMarry(player.getName());
								DataManager.getInstance().getLocal(player).setMarry(dplayer.getName());

								DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
								LocalDateTime now = LocalDateTime.now();
								String date = dateTimeFormatter.format(now);

								DataManager.getInstance().getLocal(dplayer).setMarryDate(date);
								DataManager.getInstance().getLocal(player).setMarryDate(date);

								Broadcaster.broadcastMessages("       ", "#ff03e2█&f█#ff03e2█&f█#ff03e2█", "&f█████                &f&l<#ff03e2&l!&f&l> #ff03e2&lSLUB WZIELI &f&l<#ff03e2&l!&f&l>",
										"&f█████" + ChatUtil.centerString("#8c8c8c" + player.getName() + " #ff03e2❤ #8c8c8c" + dplayer.getName(), 3, 0), "#ff03e2█&f███#ff03e2█" + ChatUtil.centerString(" &fCaly serwer zyczy wam wszystkiego dobrego!", 0, 1), "#ff03e2██&f█#ff03e2██", "");
								ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cMozesz pocalowac swoja milosc, zblizajac sie do niej i przytrzymujac prawy przycisk myszy (#fc7474PPM#8c8c8c)");
								ChatManager.sendMessage(dplayer, Configuration.SERVER_FULL_PREFIX + "#8c8c8cMozesz pocalowac swoja milosc, zblizajac sie do niej i przytrzymujac prawy przycisk myszy (#fc7474PPM#8c8c8c)");
								ServerUtil.reloadContentsGlobal();
								return true;
							}

							ChatManager.sendNotification(player, "Nie wiadomo mi nic o waszym slubie!", ChatManager.NotificationType.ERROR);
							return true;
						}

					ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
					return true;
				}
				else if (args[0].equalsIgnoreCase("odrzuc"))
				{
					if (length == 1)
					{
						ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, od ktorego otrzymales zareczyny! Wzor komendy: #ffc936/slub odrzuc <gracz>");
						return true;
					}

					final String potencial_player = args[1];

					for (Player dplayer : Bukkit.getOnlinePlayers())
						if (dplayer.getName().equalsIgnoreCase(potencial_player))
						{
							if (getMarry(dplayer) == player)
							{
								ChatManager.sendMessage(player, "#f83044&l» &cZareczyny od gracza " + ChatUtil.returnPlayerColor(player) + player.getName() + " &czostaly odrzucone!");
								ChatManager.sendMessage(dplayer, "#f83044&l» #fc7474Twoje zarczyny do gracza " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + " #fc7474zostaly odrzucone, F!");
								setMarry(dplayer, null);
								return true;
							}

							ChatManager.sendNotification(player, "Nic mi nie wiadomo o zadnym slubie!", ChatManager.NotificationType.ERROR);
							return true;
						}

					ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
					return true;
				}
				else if (args[0].equalsIgnoreCase("rozwod"))
				{
					if (DataManager.getInstance().getLocal(player).getMarry() == null)
					{
						ChatManager.sendNotification(player, "Nawet nie jestes w zwiazku!", ChatManager.NotificationType.ERROR);
						return true;
					}

					if (length == 1)
					{
						ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, od ktorego chcesz sie uwolnic! Wzor komendy: #ffc936/slub rozwod <gracz>");
						return true;
					}

					final String potencial_player = args[1];

					for (Player dplayer : Bukkit.getOnlinePlayers())
						if (dplayer.getName().equalsIgnoreCase(potencial_player))
						{
							if (DataManager.getInstance().getLocal(player).getMarry().equalsIgnoreCase(dplayer.getName()))
							{
								setMarry(player, null);
								setMarry(dplayer, null);
								DataManager.getInstance().getLocal(dplayer).setMarryDate("none");
								DataManager.getInstance().getLocal(player).setMarryDate("none");
								DataManager.getInstance().getLocal(dplayer).setMarryLevel(0);
								DataManager.getInstance().getLocal(player).setMarryLevel(0);

								Broadcaster.broadcastMessages("   ", "#8c8c8c█&f█#8c8c8c█&f█#8c8c8c█", "&f█████              &f&l<#8c8c8c&l!&f&l> #8c8c8c&lROZWOD WZIELI &f&l<#8c8c8c&l!&f&l>",
										"&f█████           #8c8c8c" + player.getName() + " #8c8c8c</3 #8c8c8c" + dplayer.getName(), "#8c8c8c█&f███#8c8c8c█       &fMamy nadzieje ze do siebie wrocicie!", "#8c8c8c██&f█#8c8c8c██", "");
								ServerUtil.reloadContentsGlobal();
								return true;
							}
							else
								ChatManager.sendNotification(player, "Wy nawet nie jestescie razem!", ChatManager.NotificationType.ERROR);

							return true;
						}

					ChatManager.sendNotification(player, "Twoja milosc musi byc on-line!", ChatManager.NotificationType.ERROR);
					return true;
				}
				else if (args[0].equalsIgnoreCase("poziom"))
				{
					if (DataManager.getInstance().getLocal(player).getMarry() == null)
					{
						ChatManager.sendNotification(player, "Nawet nie jestes w zwiazku!", ChatManager.NotificationType.ERROR);
						return true;
					}

					ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cSystem poziomow pokazuje moc milosci w zwiazku, poziom mozna zwiekszyc calujac sie lub po prostu bedac razem przez dlugi czas! Jezeli chcesz zobaczyc poziom twojego slubu, wpisz komende #fc7474/slub status");
					return true;
				}
				else if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("info"))
				{
					if (length == 1)
					{
						if (DataManager.getInstance().getLocal(player).getMarry() == null)
						{
							ChatManager.sendMessages(player, "         ", "#ff03e2█&f█#ff03e2█&f█#ff03e2█", "&f█████   #ff03e2* #8c8c8cObecnie jestes singlem",
									"&f█████   #ff03e2* #8c8c8cData twojego slubu bedzie w przyszlosci ", "#ff03e2█&f███#ff03e2█   #ff03e2* #8c8c8cPoziom % alkoholu we krwi wynosi...", "#ff03e2██&f█#ff03e2██", "");
							return true;
						}

						final int timexp = TimeUtil.getDifferenceInMinutes(DataManager.getInstance().getLocal(player).getMarryDate()) / 6;
						final int xp = DataManager.getInstance().getLocal(player).getMarryLevel() + DataManager.getInstance().getLocal(null).getMarryLevel(DataManager.getInstance().getLocal(player).getMarry()) + timexp;
						final int level = xp / 100;
						final String s = Integer.toString(xp);
						String percentage;
						if (xp < 10)
							percentage = "0";
						else
						{
							percentage = Integer.toString(xp).charAt(s.length() - 2) + "0";
							if (percentage.equalsIgnoreCase("00"))
								percentage = "0";
						}

						ChatManager.sendMessages(player, "         ", "#ff03e2█&f█#ff03e2█&f█#ff03e2█", "&f█████   #ff03e2* #8c8c8cJestes w zwiazku z " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(player).getMarry()) + DataManager.getInstance().getLocal(player).getMarry(),
								"&f█████   #ff03e2* #8c8c8cData waszego slubu to " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(player).getMarry()) + DataManager.getInstance().getLocal(player).getMarryDate(), "#ff03e2█&f███#ff03e2█   #ff03e2* #8c8c8cPoziom waszego slubu wynosi " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(player).getMarry()) + level + " #8c8c8c(" + percentage + "%)", "#ff03e2██&f█#ff03e2██", "");
						return true;
					}

					String dplayer = args[1];

					if (!DataManager.getInstance().getData().playerList().contains(dplayer))
					{
						ChatManager.sendNotification(player, "Ten gracz tu kiedykolwiek gral czy tak z dupy go podales?", ChatManager.NotificationType.ERROR);
						return true;
					}

					if (DataManager.getInstance().getLocal(null).getMarry(dplayer) == null)
					{
						ChatManager.sendMessages(player, "         ", "#ff03e2█&f█#ff03e2█&f█#ff03e2█", "&f█████   #ff03e2* #8c8c8cGracz " + ChatUtil.returnPlayerColor(dplayer) + dplayer.toLowerCase() + " #8c8c8cjest singlem",
								"&f█████   #ff03e2* #8c8c8cData jego slubu bedzie w przyszlosci ", "#ff03e2█&f███#ff03e2█   #ff03e2* #8c8c8cPoziom % alkoholu we krwi wynosi...", "#ff03e2██&f█#ff03e2██", "");
						return true;
					}

					final int timexp = TimeUtil.getDifferenceInMinutes(DataManager.getInstance().getLocal(null).getMarryDate(dplayer)) / 6;
					final int xp = DataManager.getInstance().getLocal(player).getMarryLevel() + DataManager.getInstance().getLocal(null).getMarryLevel(DataManager.getInstance().getLocal(player).getMarry()) + timexp;
					final int level = xp / 100;
					final String s = Integer.toString(xp);
					String percentage;
					if (xp < 10)
						percentage = "0";
					else
					{
						percentage = Integer.toString(xp).charAt(s.length() - 2) + "0";
						if (percentage.equalsIgnoreCase("00"))
							percentage = "0";
					}

					ChatManager.sendMessages(player, "         ", "#ff03e2█&f█#ff03e2█&f█#ff03e2█", "&f█████   #ff03e2* #8c8c8cGracz " + ChatUtil.returnPlayerColor(dplayer) + dplayer.toLowerCase() + " #8c8c8cjest z " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(null).getMarry(dplayer)) + DataManager.getInstance().getLocal(null).getMarry(dplayer),
							"&f█████   #ff03e2* #8c8c8cData ich slubu to " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(null).getMarry(dplayer)) + DataManager.getInstance().getLocal(null).getMarryDate(dplayer), "#ff03e2█&f███#ff03e2█   #ff03e2* #8c8c8cPoziom ich slubu wynosi " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(null).getMarry(dplayer)) + level + " #8c8c8c(" + percentage + "%)", "#ff03e2██&f█#ff03e2██", "");
					return true;
				}

				if (DataManager.getInstance().getLocal(player).getMarry() != null)
				{
					ChatManager.sendNotification(player, "Nie zdradzaj swojego partnera(ki), " + ChatUtil.returnPlayerColor(DataManager.getInstance().getLocal(player).getMarry()) + DataManager.getInstance().getLocal(player).getMarry() + "!", ChatManager.NotificationType.ERROR);
					return true;
				}

				final String potencial_player = args[0];

				for (Player dplayer : Bukkit.getOnlinePlayers())
					if (dplayer.getName().equalsIgnoreCase(potencial_player))
					{
						if (dplayer.getName().equalsIgnoreCase(player.getName()))
						{
							ChatManager.sendNotification(player, "Nie mozesz byc w zwiazku sam ze soba narcyzie!", ChatManager.NotificationType.ERROR);
							return true;
						}

						setMarry(player, null);
						setMarry(player, dplayer);
						ChatManager.sendMessage(player, "#f83044&l» &cWyslano zareczyny do gracza " + ChatUtil.returnPlayerColor(player) + dplayer.getName() + "!");
						ChatManager.sendMessage(dplayer, "#f83044&l» &cOtrzymano zareczyny od gracza " + ChatUtil.returnPlayerColor(player) + player.getName() + "!\n#fc7474&o/slub akceptuj/odrzuc (gracz)&f&o - zaakceptowanie/odrzucenie\n#fc7474&o" + Configuration.SERVER_MARRY_REQUEST_TIME + " sekund&f&o - czas oczekiwania");
						VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#ff03e2❤", 0, 20, 20);

						new BukkitRunnable() { @Override public void run() {
							setMarry(player, null);
						} }.runTaskLaterAsynchronously(Main.getInstance(), 20*Configuration.SERVER_MARRY_REQUEST_TIME);
						return true;
					}

				ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
			}
			return true;
		}
		
		return false;
	}
}