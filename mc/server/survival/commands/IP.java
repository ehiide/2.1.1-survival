package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IP
implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if (sender instanceof Player)
        {
            final Player player = (Player) sender;

            if (!DataManager.getInstance().getLocal(player).getRank().equalsIgnoreCase("administrator"))
            {
                ChatManager.sendNotification(player, "Chcialbys poznac kogos IP co? Musisz wiedziec, ze hakerami sa tylko admini!", ChatManager.NotificationType.ERROR);
                return true;
            }

            if (args.length == 0)
                ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTwoj adres IP to: " + ChatUtil.returnPlayerColor(player) + Objects.requireNonNull(player.getAddress()).getAddress() + "!");
            else
            {
                final String potencial_player = args[0];

                for (Player dplayer : Bukkit.getOnlinePlayers())
                    if (dplayer.getName().equalsIgnoreCase(potencial_player))
                    {
                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cAdres IP " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + "#8c8c8c to "  + ChatUtil.returnPlayerColor(dplayer)+ Objects.requireNonNull(dplayer.getAddress()).getAddress() + "!");
                        return true;
                    }

                ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
            }
            return true;
        }

        return false;
    }
}