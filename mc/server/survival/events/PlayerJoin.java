package mc.server.survival.events;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.managers.FileManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerJoin 
implements Listener
{
	@EventHandler
	public void onEvent(PlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		
		Player player = event.getPlayer();
		player.setTicksLived(1);

		if ((boolean) FileManager.getInstance().getConfigValue("function.whitelist.status"))
		{
			List<String> players = (List<String>) FileManager.getInstance().whitelist().get("whitelist");

			if (!players.contains(player.getName().toLowerCase()))
			{
				if ((boolean) FileManager.getInstance().getConfigValue("function.whitelist.join-notify"))
					ChatManager.sendAdminMessage(Configuration.SERVER_FULL_PREFIX + "&eGracz " + player.getName() + " probowal sie wpierdolic na serwer, ale zapomnial, ze nie ma go na liscie!");

				player.kickPlayer(ColorUtil.formatHEX("\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474█████\n#fc7474██&f█#fc7474██\n\n" +
						"#f83044&lUTRACONO POLACZENIE\n#fc7474Zostales wyrzucony z serwera!\n\n#8c8c8cPowod: #fc7474Whitelista? Slyszales o tym?!\n#8c8c8cSprawca: #fc7474Server" +
						"\n\n#666666&o(Nastepnym razem wymiar kary moze byc surowszy!)"));
				return;
			}
		}

		player.teleport(WorldUtil.SURVIVAL_SPAWN);
		player.setWalkSpeed(0.2F);

		ServerUtil.loginAsync(player);
		DataManager.getInstance().getLocal(player).handlePlayer();

		if ((boolean) FileManager.getInstance().getConfigValue("function.chemistry.status"))
			NeuroManager.apply(player);

		ServerUtil.reloadContents(player);

		if (DataManager.getInstance().getLocal(player).isDungeoned())
			player.teleport(WorldUtil.SURVIVAL_DUNGEON);

		player.setMaxHealth(20.0 + (4.0D * DataManager.getInstance().getLocal(player).getUpgradeLevel(player.getName(), "vitality")));
		if (TimeUtil.getDifferenceInMinutes(DataManager.getInstance().getLocal(player).getLogged()) > 60*24*7)
			QuestUtil.manageQuest(player, 10);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			onlinePlayer.showPlayer(player);
	}
	
	@EventHandler
	public void onEvent(PlayerQuitEvent event)
	{
		event.setQuitMessage(null);
		
		Player player = event.getPlayer();

		if (player.getTicksLived() < 40) return;
		
		DataManager.getInstance().getLocal(player).setLastLocation(player.getLocation());
		Broadcaster.broadcastMessage(Configuration.SERVER_FULL_PREFIX + "Gracz " + player.getName() + " opuscil serwer!");
		VisualUtil.spawnFirework(player.getLocation());

		PacketUtil.cancelTasks(player);
	}
}