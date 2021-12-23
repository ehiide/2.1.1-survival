package mc.server.survival.commands;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.TimeUtil;
import mc.server.survival.utils.VisualUtil;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Odkuj
implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if (sender instanceof Player)
        {
            final Player player = (Player) sender;
            final int length = args.length;

            if (!DataManager.getInstance().getLocal(player).getRank().equalsIgnoreCase("administrator"))
            {
                ChatManager.sendNotification(player, "Tylko administracja ma prawo uwolnic graczy!", ChatManager.NotificationType.ERROR);
                return true;
            }

            if (length == 0)
            {
                ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz nazwe gracza, ktorego chcesz uwolnic!");
                return true;
            }
            else
            {
                final String potencial_player = args[0];

                for (Player dplayer : Bukkit.getOnlinePlayers())
                    if (dplayer.getName().equalsIgnoreCase(potencial_player))
                    {
                        if (!(TimeUtil.getDifferenceInSeconds(DataManager.getInstance().getLocal(dplayer).getDungeoned(dplayer.getName())) < DataManager.getInstance().getLocal(dplayer).getDungeonLength(dplayer.getName())))
                        {
                            ChatManager.sendNotification(player, "Ten gracz nie jest nawet zakuty, wiemy ze masz dobre serduszko ale bez przesady!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        String unbanner = player.getName();

                        if (length == 2 && args[1].equalsIgnoreCase("-ukryj"))
                        {
                            unbanner = "anonima";
                        }

                        dplayer.teleport(WorldUtil.SURVIVAL_SPAWN);
                        DataManager.getInstance().getLocal(dplayer).setDungeonLength(0);
                        Broadcaster.broadcastMessages("       ", "#80ff1f██&f█#80ff1f██", "#80ff1f██&f█#80ff1f██                  &f&l<#80ff1f&l!&f&l> #80ff1f&lODKUCIE &f&l<#80ff1f&l!&f&l>",
                                "#80ff1f██&f█#80ff1f██" + ChatUtil.centerString("#8c8c8cGracz #80ff1f" + dplayer.getName() + " #8c8c8czostal uwolniony!", 3, 0), "#80ff1f█████" + ChatUtil.centerString(" &aOznacza to, ze wychodzi na wolnosc!", 0, 1), "#80ff1f██&f█#80ff1f██", "");
                        ChatManager.sendMessage(dplayer, Configuration.SERVER_FULL_PREFIX + "#80ff1fZostales uwolniony przez " + unbanner + ", lecz uwazaj bo nastepnym razem moze nie byc taki mily!");
                        VisualUtil.showDelayedTitle(dplayer, " ", "#80ff1f✔", 0, 10, 10);
                        return true;
                    }

                ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
            }
            return true;
        }

        return false;
    }
}