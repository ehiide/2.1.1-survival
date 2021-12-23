package mc.server.survival.commands;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.ColorUtil;
import mc.server.survival.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Powiedz implements CommandExecutor
{
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if (sender instanceof Player)
        {
            final Player player = (Player) sender;
            final int length = args.length;

            if (!DataManager.getInstance().getLocal(player).getRank().equalsIgnoreCase("administrator"))
            {
                Broadcaster.broadcastMessage(ColorUtil.formatHEX("#80ff1f[" + TimeUtil.hour() + ":" + TimeUtil.minute() + "#80ff1f] " + ChatUtil.getGangInChat(DataManager.getInstance().getLocal(player).getGang()) + ChatUtil.returnMarryPrefix(player) +  "&r" + ChatUtil.returnPlayerColor(player) + Objects.requireNonNull(player.getPlayer()).getName() + "#8c8c8c Debil ze mnie."));
                return true;
            }

            if (length == 0)
            {
                ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz nazwe gracza, aby kontynuowac!");
                return true;
            }
            else
            {
                final String potencial_player = args[0];

                for (Player dplayer : Bukkit.getOnlinePlayers())
                    if (dplayer.getName().equalsIgnoreCase(potencial_player))
                        if (length == 1)
                        {
                            ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz swoja falszywa wiadomosc, ktora chcesz wyslac!");
                            return true;
                        }
                        else
                        {
                            StringBuilder message = new StringBuilder();

                            for (int word = 1; word < length; word++)
                                if (word + 1 >= length)
                                    message.append(args[word].toLowerCase());
                                else
                                    message.append(args[word].toLowerCase()).append(" ");

                            String fakeMessage = ChatUtil.getPlaceholder(dplayer, ChatUtil.applyCorrection(message.toString()));
                            Broadcaster.broadcastMessage(ColorUtil.formatHEX("#80ff1f[" + TimeUtil.hour() + ":" + TimeUtil.minute() + "#80ff1f] " + ChatUtil.getGangInChat(DataManager.getInstance().getLocal(dplayer).getGang()) + ChatUtil.returnMarryPrefix(dplayer) +  "&r" + ChatUtil.returnPlayerColor(dplayer) + Objects.requireNonNull(dplayer.getPlayer()).getName() + "#8c8c8c " + fakeMessage));
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