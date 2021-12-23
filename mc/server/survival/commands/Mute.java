package mc.server.survival.commands;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.QuestUtil;
import mc.server.survival.utils.TimeUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mute implements CommandExecutor
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
                ChatManager.sendNotification(player, "Wyciszac graczy moze tylko administracja!", ChatManager.NotificationType.ERROR);
                return true;
            }

            if (length == 0)
            {
                ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz nazwe gracza, ktorego chcesz wyciszyc!");
                return true;
            }
            else
            {
                final String potencial_player = args[0];

                for (Player dplayer : Bukkit.getOnlinePlayers())
                    if (dplayer.getName().equalsIgnoreCase(potencial_player))
                    {
                        if (TimeUtil.getDifferenceInSeconds(DataManager.getInstance().getLocal(dplayer).getMute(dplayer.getName())) < DataManager.getInstance().getLocal(dplayer).getMuteLength(dplayer.getName()))
                        {
                            ChatManager.sendNotification(player, "Ten gracz jest juz wyciszony, wiec nie mozesz go wyciszyc!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (length == 1)
                        {
                            ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz czas, na jaki chcesz wyciszyc gracza!");
                            return true;
                        }

                        final String init = args[1];

                        if (!isCorrectTime(init))
                        {
                            ChatManager.sendNotification(player, "Podany czas nie jest obslugiwany, naucz sie cyfr!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (!isCorrectUnit(init))
                        {
                            ChatManager.sendNotification(player, "Podany format czasowy nie jest obslugiwany!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (length == 2)
                            ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz powod wyciszenia gracza!");
                        else
                        {
                            StringBuilder message = new StringBuilder();

                            for (int word = 2; word < length; word++)
                                if (word + 1 >= length)
                                    message.append(args[word].toLowerCase());
                                else
                                    message.append(args[word].toLowerCase()).append(" ");

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String date = dateTimeFormatter.format(now);
                            DataManager.getInstance().getLocal(dplayer).setMute(date);
                            DataManager.getInstance().getLocal(dplayer).setMuteLength(catchTime(init));

                            String muter = player.getName();
                            String cause = ChatUtil.applyCorrection(message.toString());

                            if (length == 3 && args[2].equalsIgnoreCase("-konsola") |
                                    args[2].equalsIgnoreCase("-server") | args[2].equalsIgnoreCase("-serwer") |
                                    args[2].equalsIgnoreCase("-survival"))
                            {
                                muter = "Server";
                                cause = "Server";
                            }

                            if (length == 3 && args[2].equalsIgnoreCase("-ukryj"))
                            {
                                muter = "anonim";
                                cause = "anonim!";
                            }

                            cause = cause.substring(0, cause.length() - 1);

                            Broadcaster.broadcastMessages("       ", "#ffc936██&f█#ffc936██", "#ffc936██&f█#ffc936██                     &f&l<#ffc936&l!&f&l> #ffc936&lMUTE &f&l<#ffc936&l!&f&l>",
                                    "#ffc936██&f█#ffc936██" + ChatUtil.centerString("#8c8c8cGracz #ffc936" + dplayer.getName() + " #8c8c8czamknal morde na okres #ffc936" + args[1].toLowerCase() + "#8c8c8c!", 5, 0), "#ffc936█████" + ChatUtil.centerString(" #f8ff26Wymiar pokuty nalozyl " + muter + "!", 1, 0), "#ffc936██&f█#ffc936██", "");
                            ChatManager.sendMessage(dplayer, Configuration.SERVER_FULL_PREFIX + "#ffc936Zostales wyciszony na czacie za: " + cause + "#ffc936!");
                            VisualUtil.showDelayedTitle(dplayer, " ", "#ffc936✖", 0, 10, 10);
                            QuestUtil.manageQuest(dplayer, 9);
                        }
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

    private boolean isCorrectUnit(String date)
    {
        final String substring = date.substring(date.length() - 1);
        final String[] units = {"s", "m", "h"};

        for (String unit : units)
        {
            if (substring.equalsIgnoreCase(unit))
            {
                return true;
            }
        }

        return false;
    }

    private int unit(String date)
    {
        final String substring = date.substring(date.length() - 1);

        if (substring.equalsIgnoreCase("s"))
            return 1;

        if (substring.equalsIgnoreCase("m"))
            return 60;

        if (substring.equalsIgnoreCase("h"))
            return 3600;

        return -1;
    }

    private boolean isCorrectTime(String date)
    {
        if (date.length() != 2 && date.length() != 3) return false;

        final String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        final String substring = date.substring(date.length() - 2, date.length() - 1);

        if (date.length() == 2)
        {
            for (String number : numbers)
            {
                if (substring.equalsIgnoreCase(number))
                    return true;
            }

            return false;
        }

        for (String number : numbers)
        {
            if (substring.equalsIgnoreCase(number))
                for (String number2 : numbers)
                    if (date.substring(0, date.length() - 2).equalsIgnoreCase(number2))
                        return true;
        }

        return false;
    }

    private int catchTime(String date)
    {
        final String time = date.substring(0, date.length() - 1);
        return Integer.parseInt(time) * unit(date);
    }
}