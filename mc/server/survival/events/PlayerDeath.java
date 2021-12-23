package mc.server.survival.events;

import mc.server.Broadcaster;
import mc.server.survival.files.Main;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.QuestUtil;
import mc.server.survival.utils.ServerUtil;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeath implements Listener
{
	@EventHandler
	public void onEvent(PlayerDeathEvent event)
	{
		event.setDeathMessage(null);

		final Player death = event.getEntity();
		
		if (event.getEntity().getKiller() != null)
		{
			final Player killer = event.getEntity().getKiller();

			DataManager.getInstance().getLocal(killer).setKills(DataManager.getInstance().getLocal(killer).getKills() + 1);
			ServerUtil.reloadContents(killer);

			Broadcaster.broadcastMessage("#f83044[⚔] #8c8c8cGracz " + death.getName() + " (#80ff1f" + DataManager.getInstance().getLocal(death).getKills() + "⚔#8c8c8c/#fc7474" + DataManager.getInstance().getLocal(death).getDeaths() + "☠#8c8c8c) zostal zabity przez " + killer.getName() + " (#80ff1f" + DataManager.getInstance().getLocal(killer).getKills() + "⚔#8c8c8c/#fc7474" + DataManager.getInstance().getLocal(killer).getDeaths() + "☠#8c8c8c)!");
			QuestUtil.manageQuest(killer, 6);

			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta skull_meta = (SkullMeta) skull.getItemMeta();
			assert skull_meta != null;
			skull_meta.setOwningPlayer(Bukkit.getOfflinePlayer(death.getName()));
			skull.setItemMeta(skull_meta);
			skull.setItemMeta(skull_meta);

			event.getDrops().add(skull);
		}
		else
			if (DataManager.getInstance().getLocal(death).getMoney() >= 10)
			{
				DataManager.getInstance().getLocal(death).setMoney(DataManager.getInstance().getLocal(death).getMoney() - (int) (DataManager.getInstance().getLocal(death).getMoney() * getMoneyBack(death)));
				ServerUtil.reloadContents(death);
			}

		new BukkitRunnable() { @Override public void run() {
			death.spigot().respawn();
			death.teleport(WorldUtil.SURVIVAL_SPAWN);
			death.setMaxHealth(20.0 + (4.0D * DataManager.getInstance().getLocal(death).getUpgradeLevel(death.getName(), "vitality")));
			death.setHealth(death.getMaxHealth());

			if (DataManager.getInstance().getLocal(death).isDungeoned())
				death.teleport(WorldUtil.SURVIVAL_DUNGEON);
		} }.runTaskLater(Main.getInstance(), 1L);

		DataManager.getInstance().getLocal(death).setDeaths(DataManager.getInstance().getLocal(death).getDeaths() + 1);
		ServerUtil.reloadContents(death);
	}

	private double getMoneyBack(Player player)
	{
		return 0.1 - (0.02 * DataManager.getInstance().getLocal(player).getUpgradeLevel(player.getName(), "luck"));
	}
}