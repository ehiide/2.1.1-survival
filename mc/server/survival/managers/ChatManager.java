package mc.server.survival.managers;

import mc.server.survival.files.Configuration;
import mc.server.survival.utils.ColorUtil;
import mc.server.survival.utils.SoundUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChatManager 
{
	public static void sendMessage(final Player player, String message)
	{
		player.sendMessage(ColorUtil.formatHEX(message));
	}

	public static void sendMessages(final Player player, String... message)
	{
		for (String line : message)
			player.sendMessage(ColorUtil.formatHEX(line));
	}

	public static void sendAdminMessage(final String message)
	{
		for (Player player : Bukkit.getOnlinePlayers())
			if (DataManager.getInstance().getLocal(player).get(player.getName().toLowerCase()) != null)
				if (DataManager.getInstance().getLocal(player).getRank().equalsIgnoreCase("administrator"))
					sendMessage(player, message);
	}

	public enum NotificationType
	{
		SUCCESS, ERROR, INFORMATION, LOGGING;
	}

	public static void sendNotification(final Player player, String notify, final NotificationType type)
	{
		final String notification;

		switch (type)
		{
			case SUCCESS -> notification = "&a" + notify;
			case ERROR -> notification = "#fc7474" + notify;
			case INFORMATION -> notification = "#8c8c8c" + notify;
			case LOGGING -> notification = "&e" + notify;
			default -> notification = notify;
		}

		sendMessage(player, (type == NotificationType.LOGGING ? Configuration.SERVER_LOGGING_PREFIX : Configuration.SERVER_FULL_PREFIX) + notification);

		if (type == NotificationType.SUCCESS)
			SoundUtil.playPlayerSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 4, 4);
		else if (type == NotificationType.ERROR)
		{
			VisualUtil.showDelayedTitle(player, "#fc7474✖", "", 0, 20, 20);
			SoundUtil.playPlayerSound(player, Sound.BLOCK_ANVIL_DESTROY, 4, 4);
		}
		else if (type == NotificationType.LOGGING)
			VisualUtil.showDelayedTitle(player, "#fff203⌛", "", 0, 20, 20);
	}
}