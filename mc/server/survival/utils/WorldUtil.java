package mc.server.survival.utils;

import mc.server.Logger;
import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldUtil 
{
	@Nonnull
	public static final Location SURVIVAL_SPAWN = new Location(getWorld("survival"), 410, 68.5, -20, 0, 0);
	public static final Location SURVIVAL_CHURCH = new Location(getWorld("survival"), 455, 70, -25, 0, 0);
	public static final Location SURVIVAL_DUNGEON = new Location(getWorld("survival"), 373, 45, -53, -90, 0);
	public static final Location SUROWCE_SPAWN = new Location(getWorld("surowce"), 0, 120, 0, 0, 0);

	private static final String[] worlds = {"survival", "survival_nether", "survival_the_end", "surowce"};
	private static int preparedWorlds = 0;
	private static int loadedWorlds = 0;

	public static World getWorld(String name)
	{
		return Bukkit.getServer().createWorld(new WorldCreator(name));
	}

	public static List<World> getWorlds()
	{
		List<World> allWorlds = new ArrayList<>();

		for (String worldName : worlds)
			allWorlds.add(getWorld(worldName));

		return allWorlds;
	}

	public static void loadWorlds()
	{
		for (String world : worlds)
			loadWorld(world);
	}

    public static void loadWorld(String name)
	{
		if (preparedWorlds++ >= worlds.length - 1 /* Due to lobby world */)
			Logger.asyncLog("&7Zaladowano swiaty: " + loadedWorlds + "/" + worlds.length);

		WorldCreator worldCreator = new WorldCreator(name);
		Bukkit.getServer().createWorld(worldCreator);

		if (Bukkit.getWorld(name) == null)
			Logger.asyncLog("&cNie udalo sie zaladowac swiata: " + name + ".");

		applyGameRules(name);
		loadedWorlds++;
	}

	private static void applyGameRules(final String name)
	{
		World world = getWorld(name);

		world.setGameRule(GameRule.MOB_GRIEFING, false);
		world.setGameRule(GameRule.DO_PATROL_SPAWNING, !name.equalsIgnoreCase("survival"));
		world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 50);
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, true);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
		world.setGameRule(GameRule.KEEP_INVENTORY, false);
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, 2);
	}

	public static void teleportRandomly(Player player)
	{
		teleportRandomly(player, player.getWorld().getName());
	}

	public static void teleportRandomly(Player player, String world)
	{
		new BukkitRunnable() { @Override public void run() {
			int x = 2000 - new Random().nextInt(4000);
			int z = 2000 - new Random().nextInt(4000);
			int y = player.getWorld().getHighestBlockYAt(x, z);

			new BukkitRunnable() { @Override public void run() {
				player.teleport(new Location(getWorld(world), x, y, z));
			} }.runTask(Main.getInstance());
		} }.runTaskAsynchronously(Main.getInstance());
	}

	public enum WorldType
	{
		SURVIVAL,
		SURWOCE,
		DIMENSIONABLE;
	}

	public static void queueWorldChange(@NotNull Player player, final WorldType worldType)
	{
		if (worldType == WorldType.SURVIVAL)
		{
			ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja na lobby serwera... #fff203⌛");
			player.teleport(SURVIVAL_SPAWN);
		}
		else if (worldType == WorldType.SURWOCE)
		{
			ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja na swiat surowcowy... #fff203⌛");
			teleportRandomly(player, "surowce");
		}
		else if (worldType == WorldType.DIMENSIONABLE)
		{
			if (Configuration.SERVER_BLOCK_NETHER)
			{
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#fc7474Wrota do tego swiata sa obecnie zamkniete!");
				VisualUtil.showDelayedTitle(player, "&c✖", "", 0, 20, 20);
			}
		}

		new BukkitRunnable() { @Override public void run() {
			VisualUtil.showServerChangeTitle(player);
			ServerUtil.reloadContents(player);

			if (worldType != WorldType.DIMENSIONABLE)
				VisualUtil.spawnFirework(player.getLocation());
		} }.runTaskLater(Main.getInstance(), 20L);
	}

	public static class DropChecker
	{
		private static boolean isDrop(final Entity entity)
		{
			return entity.getCustomName() != null && entity instanceof Item && entity.getTicksLived() > 600;
		}

		public static void runTask()
		{
			new BukkitRunnable() { @Override public void run() {
				final List<World> worlds = getWorlds();

				for (final World world : worlds)
					for (final Entity entity : world.getEntities())
						if (isDrop(entity)) entity.remove();
			} }.runTaskTimer(Main.getInstance(), 3000, 3000);
		}
	}
}