package mc.server.survival.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ServerUtil 
{
	public static void reloadContents(final Player player)
	{
		ScoreboardUtil.showScoreboard(player);
		TablistUtil.showTablist(player);
		TablistUtil.showPlayerTag(player);
		NametagUtil.showNametag(player);
	}

	public static void reloadContentsGlobal()
	{
		Bukkit.getOnlinePlayers().forEach(ServerUtil::reloadContents);
	}
	
	public static int getPing(final Player player)
	{
		return player.getPing();
	}

	public static boolean getPremiumState(final String name)
	{
		try
		{
			final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			StringBuilder result = new StringBuilder();

			while ((line = reader.readLine()) != null)
				result.append(line);

			return !result.toString().equals("");
		}
		catch (IOException e) { e.printStackTrace(); }

		return false;
	}

	public static String getUUID(Player player)
	{
		try
		{
			final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName());
			final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			StringBuilder result = new StringBuilder();

			while ((line = reader.readLine()) != null)
				result.append(line);

			String reply = result.toString();

			int z = 0;

			for (int x = 0; x < reply.length(); x++)
				if (reply.substring(x, x + 1).equalsIgnoreCase("\""))
					if (++z == 7)
						for (int y = x + 1; y < reply.length(); y++)
							if (reply.substring(y, y + 1).equalsIgnoreCase("\""))
								if (++z == 8)
									return reply.substring(x + 1, y);
		}
		catch (IOException e) { e.printStackTrace(); }

		return null;
	}

	public static void login(@NotNull Player player)
	{
		try
		{
			ChatManager.sendNotification(player, "Nawiazywanie polaczenia z serwerami Mojang...", ChatManager.NotificationType.LOGGING);

			if (getPremiumState(player.getName()))
			{
				ChatManager.sendNotification(player, "Dostosowywanie konta premium...", ChatManager.NotificationType.LOGGING);

				final CraftPlayer craftPlayer = ((CraftPlayer) player);
				GameProfile profile = craftPlayer.getProfile();

				final URL url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", getUUID(player)));
				final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

				if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK)
				{
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					String line;
					StringBuilder result = new StringBuilder();

					while ((line = reader.readLine()) != null)
						result.append(line);

					String reply = result.toString();

					String skin = null, signature = null;

					int z = 0;

					for (int x = 0; x < reply.length(); x++)
						if (reply.substring(x, x + 1).equalsIgnoreCase("\""))
							if (++z == 17)
								for (int y = x + 1; y < reply.length(); y++)
									if (reply.substring(y, y + 1).equalsIgnoreCase("\""))
										if (++z == 18)
											skin = reply.substring(x + 1, y);

					z = 0;

					for (int x = 0; x < reply.length(); x++)
						if (reply.substring(x, x + 1).equalsIgnoreCase("\""))
							if (++z == 21)
								for (int y = x + 1; y < reply.length(); y++)
									if (reply.substring(y, y + 1).equalsIgnoreCase("\""))
										if (++z == 22)
											signature = reply.substring(x + 1, y);

					profile.getProperties().put("textures", new Property("textures", skin, signature));

					PacketUtil.sendPacket(player,
							new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, ((CraftPlayer) player).getHandle()),
							new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, ((CraftPlayer) player).getHandle()));

					ChatManager.sendNotification(player, "Odswiezanie sesji gracza...", ChatManager.NotificationType.LOGGING);

					new BukkitRunnable() { @Override public void run() {
						player.teleport(WorldUtil.SUROWCE_SPAWN);
						player.teleport(WorldUtil.SURVIVAL_SPAWN);
					} }.runTask(Main.getInstance());
				}
				else
					ChatManager.sendNotification(player, "#fc7474Wystapil nieoczekiwany problem podczas logowania.", ChatManager.NotificationType.LOGGING);
			}
			else
				ChatManager.sendNotification(player, "Dostosowywanie konta non-premium...", ChatManager.NotificationType.LOGGING);

			ChatManager.sendNotification(player, "Zalogowano.", ChatManager.NotificationType.LOGGING);
			Broadcaster.broadcastMessage(Configuration.SERVER_FULL_PREFIX + "Gracz " + player.getName() + " dolaczyl na serwer!");

			new BukkitRunnable() { @Override public void run() {
				VisualUtil.showServerChangeTitle(player);
				VisualUtil.spawnFirework(player.getLocation());
			} }.runTask(Main.getInstance());

			if (player.hasPlayedBefore())
				new BukkitRunnable() { @Override public void run() {
					ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX +
							"Poniewaz znajdowales sie poza spawnem, teleportowano Cie do niego. " +
							"Jezeli chcesz powrocic na swoje ostatnie miejsce wpisz komende #fc7474/powrot!");
				} }.runTaskLaterAsynchronously(Main.getInstance(), 60L);
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	public static void loginAsync(@NotNull Player player)
	{
		new BukkitRunnable() { @Override public void run() {
			login(player);
		} }.runTaskLaterAsynchronously(Main.getInstance(), 2L);
	}
}