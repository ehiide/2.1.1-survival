package mc.server.survival.events;

import mc.server.survival.commands.AllowedCommands;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

public class CommandPreProcess 
implements Listener
{
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) 
	{
		if (DataManager.getInstance().getLocal(event.getPlayer()).isDungeoned())
			event.setCancelled(true);

		if (!event.isCancelled())
		{
			final String string = event.getMessage().split(" ")[0];
			final HelpTopic helptopic = Bukkit.getServer().getHelpMap().getHelpTopic(string);
			final Player player = event.getPlayer();

			if (helptopic != null)
			{
				for (String command : AllowedCommands.allowedCommnads)
					if (string.equalsIgnoreCase("/" + command))
						return;


				wrongCommand(event, player);
			}
			else
				wrongCommand(event, event.getPlayer());
		}
	}

	private void wrongCommand(PlayerCommandPreprocessEvent event, final Player player)
	{
		ChatManager.sendNotification(player, "O chuj Ci chodzi! Ta komenda nie istnieje w bazie danych serwera!", ChatManager.NotificationType.ERROR);
		event.setCancelled(true);
	}
}