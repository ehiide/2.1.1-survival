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

public class Odpowiedz implements CommandExecutor
{
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if (sender instanceof Player)
        {
            final Player player = (Player) sender;
            final int length = args.length;

            if (Wiadomosc.getReply(player) == null)
            {
                ChatManager.sendNotification(player, "Nie masz do kogo odpowiedziec!", ChatManager.NotificationType.ERROR);
                return true;
            }

            if (length == 0)
            {
                ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz swoja wiadomosc! Wzor komendy: #ffc936/odpowiedz <wiadomosc>");
                return true;
            }
            else
            {
                final String potencial_player = Wiadomosc.getReply(player).getName();

                for (Player dplayer : Bukkit.getOnlinePlayers())
                    if (dplayer.getName().equalsIgnoreCase(potencial_player))
                    {
                        StringBuilder message = new StringBuilder();

                        for (int word = 0; word < length; word++)
                            if (word + 1 >= length)
                                message.append(args[word].toLowerCase());
                            else
                                message.append(args[word].toLowerCase()).append(" ");

                        ChatManager.sendMessage(player, "#f83044&l» &cDo " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + ": &r&7" + ChatUtil.applyCorrection(ChatUtil.getPlaceholder(player, message.toString())));
                        ChatManager.sendMessage(dplayer, "#f83044&l» &cWiadomosc od " + ChatUtil.returnPlayerColor(player) + player.getName() + ": &r&7" + ChatUtil.applyCorrection(ChatUtil.getPlaceholder(player, message.toString())));
                        VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#faff26✉", 0, 10, 10);

                        Wiadomosc.setReply(player, dplayer);
                        Wiadomosc.setReply(dplayer, player);

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