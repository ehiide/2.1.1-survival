package mc.server.survival.commands;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Kick
implements CommandExecutor
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
                player.kickPlayer(ColorUtil.formatHEX("\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474█████\n#fc7474██&f█#fc7474██\n\n" +
                        "#f83044&lUTRACONO POLACZENIE\n#fc7474Zostales wyrzucony z serwera!\n\n#8c8c8cPowod: #fc7474Kto komu dolki kopie ten sam w nie wpada!\n#8c8c8cSprawca: #fc7474ANTI-TOXIC" +
                        "\n\n#666666&o(Nastepnym razem wymiar kary moze byc surowszy!)"));
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
                    {
                        String kicker = player.getName();

                        if (length == 1)
                        {
                            dplayer.kickPlayer(ColorUtil.formatHEX("\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474█████\n#fc7474██&f█#fc7474██\n\n" +
                                    "#f83044&lUTRACONO POLACZENIE\n#fc7474Zostales wyrzucony z serwera!\n\n#8c8c8cPowod: #fc7474Brak powodu.\n#8c8c8cSprawca: #fc7474" + kicker +
                                    "\n\n#666666&o(Nastepnym razem wymiar kary moze byc surowszy!)"));
                        }
                        else
                        {

                            StringBuilder message = new StringBuilder();

                            for (int word = 1; word < length; word++)
                                if (word + 1 >= length)
                                    message.append(args[word].toLowerCase());
                                else
                                    message.append(args[word].toLowerCase()).append(" ");

                            String cause = ChatUtil.applyCorrection(String.valueOf(message));

                            if (length == 2 && args[1].equalsIgnoreCase("-konsola") |
                                    args[1].equalsIgnoreCase("-server") | args[1].equalsIgnoreCase("-serwer") |
                                    args[1].equalsIgnoreCase("-survival"))
                            {
                                kicker = "Server";
                                cause = "Server";
                            }

                            if (length == 2 & args[1].equalsIgnoreCase("-ukryj"))
                            {
                                kicker = "anonim";
                                cause = "Powod jest anonimowy!";
                            }

                            if (length == 2 & args[1].equalsIgnoreCase("-fake"))
                            {
                                dplayer.kickPlayer("Internal Exception: java.lang.NullPointerException");
                                return true;
                            }

                            if (length == 2 & args[1].equalsIgnoreCase("-fake:Null"))
                            {
                                dplayer.kickPlayer("Internal Exception: java.lang.NullPointerException");
                                return true;
                            }

                            if (length == 2 & args[1].equalsIgnoreCase("-fake:Packets"))
                            {
                                dplayer.kickPlayer("You are sending too many packets!");
                                return true;
                            }

                            if (length == 2 & args[1].equalsIgnoreCase("-fake:TimedOut"))
                            {
                                dplayer.kickPlayer("Timed out");
                                return true;
                            }

                            dplayer.kickPlayer(ColorUtil.formatHEX("\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474██&f█#fc7474██\n#fc7474█████\n#fc7474██&f█#fc7474██\n\n" +
                                    "#f83044&lUTRACONO POLACZENIE\n#fc7474Zostales wyrzucony z serwera!\n\n#8c8c8cPowod: #fc7474" + cause + "\n#8c8c8cSprawca: #fc7474" + kicker +
                                    "\n\n#666666&o(Nastepnym razem wymiar kary moze byc surowszy!)"));
                        }

                        Broadcaster.broadcastMessages("       ", "#fc7474██&f█#fc7474██", "#fc7474██&f█#fc7474██                     &f&l<#fc7474&l!&f&l> #fc7474&lKICK &f&l<#fc7474&l!&f&l>",
                                "#fc7474██&f█#fc7474██" + ChatUtil.centerString("#8c8c8cGracz #fc7474" + dplayer.getName() + " #8c8c8czostal wyjebany!", 3, 0), "#fc7474█████" + ChatUtil.centerString(" &cWymiar pokuty nalozyl " + kicker + "!", 0, 1), "#fc7474██&f█#fc7474██", "");
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