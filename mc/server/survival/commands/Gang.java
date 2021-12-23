package mc.server.survival.commands;

import mc.server.Broadcaster;
import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.InventoryUtil;
import mc.server.survival.utils.ServerUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class Gang
implements CommandExecutor
{
    private static HashMap<Player, Player> gang_queue = new HashMap<>();
    private static Player getGang(Player player)
    {
        return gang_queue.get(player);
    }
    private static void setGang(Player player, Player address)
    {
        gang_queue.put(player, address);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if (sender instanceof Player)
        {
            final Player player = (Player) sender;
            final int length = args.length;

            if (length == 0)
                ChatManager.sendMessages(player, "",
                         "#f83044&m---------------------------------------",
                         "#fc7474/gang stworz (nazwa) #8c8c8c- tworzy Twoj wlasny gang, musisz posiadac 1000 monet oraz nie byc w zadnym innym gangu",
                         "#fc7474/gang zapros (gracz) #8c8c8c- zaprasza gracza do Twojego gangu, nie moze byc on jednak w zadnym innym",
                         "#fc7474/gang dolacz (gracz) #8c8c8c- akceptuje zaproszenie do gangu od konkretnego gracza",
                         "#fc7474/gang wyrzuc (gracz) #8c8c8c- usuwa wskzanego gracza z Twojego gangu, wymaga rangi lidera",
                         "#fc7474/gang lider (gracz) #8c8c8c- ustawia nowego lidera gangu",
                         "#fc7474/gang baza [ustaw] #8c8c8c- teleportuje badz ustawia baze gangu, do ktorej mozesz sie nastepnie teleportowac",
                         "#fc7474/gang info [nazwa] #8c8c8c- wyswietla informacje o Twoim gangu, opcjonalnie innego",
                         "#fc7474/gang friendlyfire #8c8c8c- blokuje/odblokowuje mozliwosc wzajemnego bicia czlonkow gangu",
                         "#fc7474/gang tpall #8c8c8c- teleportuje wszystkich czlonkow gangu do lidera",
                         "#fc7474/gang ulepsz #8c8c8c- otwiera menu w sklepie, w ktorym mozesz personalizowac i ulepszac swoj gang",
                         "#fc7474/gang opusc #8c8c8c- opuszcza gang, w ktorym sie znajdujesz, jesli jestes liderem, gang sie usunie",
                         "&8&o(argumenty wymagane - (), argumewnty opcjonalne - [])",
                         "#f83044&m---------------------------------------", "");
            else
            {
                if (length > 2)
                {
                    ChatManager.sendNotification(player, "Co kurwa?! Wpisz #ffc936/gang#fc7474 i nie pierdol!", ChatManager.NotificationType.ERROR);
                    return true;
                }

                if (args[0].equalsIgnoreCase("stworz"))
                    if (length > 1)
                    {
                        if (args[1].length() > 5 || args[1].length() < 3)
                        {
                            ChatManager.sendNotification(player, "Nazwa Twojego gangu moze miec od 3 do 5 znakow!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (DataManager.getInstance().getLocal(player).getMoney() < 1000)
                        {
                            ChatManager.sendNotification(player, "Masz za malo monet, aby stworzyc gang!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (DataManager.getInstance().getLocal(null).isExist(args[1].toUpperCase()))
                        {
                            ChatManager.sendNotification(player, "Gang o takiej nazwie juz istnieje!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (DataManager.getInstance().getLocal(player).getGang() == null)
                        {
                            Broadcaster.broadcastMessages("       ", "&f█████", "&f█#85f5ff█&f█#85f5ff█&f█               &f&l<#85f5ff&l!&f&l> #85f5ff&lPOWSTAL GANG &f&l<#85f5ff&l!&f&l>",
                                    "&f█████" + ChatUtil.centerString(" #8c8c8cPrzywitajmy nowy gang, &f" + args[1].toUpperCase() + "#8c8c8c!", 2, 1), "#85f5ff█&f███#85f5ff█" + ChatUtil.centerString("&fZalozycielem gangu jest " + player.getName() + "!", 0, 1), "#85f5ff█&f█#85f5ff█&f█#85f5ff█", "");
                            DataManager.getInstance().getLocal(player).createGang(args[1].toUpperCase());
                            DataManager.getInstance().getLocal(player).setGang(args[1].toUpperCase());
                            DataManager.getInstance().getLocal(player).removeMoney(1000);
                            ServerUtil.reloadContentsGlobal();
                        }
                        else
                            ChatManager.sendNotification(player, "Znajdujesz sie juz w gangu, wiec nie mozesz stworzyc nowego!", ChatManager.NotificationType.ERROR);

                        return true;
                    }
                    else
                    {
                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe swojego gangu, pamietaj, ze moze ona miec od 3 do 5 znakow! Wzor komendy: #ffc936/gang stworz <nazwa>");
                        return true;
                    }
                else if (args[0].equalsIgnoreCase("opusc"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                        ChatManager.sendNotification(player, "Nie znajdujesz sie w zadnym gangu, wiec nie mozesz go opuscic!", ChatManager.NotificationType.ERROR);
                    else
                    {
                        ChatManager.sendNotification(player, "Pomyslnie opusciles swoj gang!", ChatManager.NotificationType.INFORMATION);
                        DataManager.getInstance().getLocal(player).removePlayer(player);
                        ServerUtil.reloadContentsGlobal();
                    }

                    return true;
                }
                else if (args[0].equalsIgnoreCase("lider"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                    {
                        ChatManager.sendNotification(player, "Ziomek, Ty nawet nie masz gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (!DataManager.getInstance().getLocal(null).getLider(DataManager.getInstance().getLocal(player).getGang()).equalsIgnoreCase(player.getName()))
                    {
                        ChatManager.sendNotification(player, "Tylko lider moze dokonac zmiany lidera!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (length < 2)
                    {
                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, ktoremu chcesz oddac stanowisko lidera! Wzor komendy: #ffc936/gang lider <gracz>");
                        return true;
                    }

                    final String potencial_lider = args[1];

                    for (Player lider : Bukkit.getOnlinePlayers())
                        if (lider.getName().equalsIgnoreCase(potencial_lider))
                        {
                            if (!DataManager.getInstance().getLocal(lider).getGang().equalsIgnoreCase(DataManager.getInstance().getLocal(player).getGang()))
                            {
                                ChatManager.sendNotification(player, "Ten gracz nie jest twoim podopiecznym!", ChatManager.NotificationType.ERROR);
                                return true;
                            }

                            if (DataManager.getInstance().getLocal(null).getLider(DataManager.getInstance().getLocal(lider).getGang()).equalsIgnoreCase(lider.getName()))
                            {
                                ChatManager.sendNotification(player, "Jestes juz na stanowisku lidera jebany narcyzie!", ChatManager.NotificationType.ERROR);
                                return true;
                            }

                            ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cUstawiles nowego lidera gangu, " + ChatUtil.returnPlayerColor(lider) + lider.getName() + "!");
                            ChatManager.sendMessage(lider, Configuration.SERVER_FULL_PREFIX + "#8c8c8cZostales nowym liderem swojego gangu!");
                            DataManager.getInstance().getLocal(player).setLider(lider);
                            return true;
                        }

                    ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);

                    return true;
                }
                else if (args[0].equalsIgnoreCase("info"))
                {
                    if (length > 1)
                    {
                       final String potencial_gang = args[1];
                       final boolean isExist = DataManager.getInstance().getLocal(null).isExist(potencial_gang);

                       if (!isExist)
                       {
                           ChatManager.sendNotification(player, "Taki gang nawet nie istnieje przyglupie!", ChatManager.NotificationType.ERROR);
                           return true;
                       }

                        String gang = potencial_gang.toUpperCase();

                        ChatManager.sendMessages(player, "       ", "&f█████", "&f█#85f5ff█&f█#85f5ff█&f█   #85f5ff* #8c8c8cWyglad prefixu gangu " + gang.toUpperCase() + " to " + ChatUtil.getGangInChat(gang),
                                "&f█████   #85f5ff* #8c8c8cObecnym liderem jest " + ChatUtil.getValidGangColor(gang) + DataManager.getInstance().getLocal(null).getLider(gang), "#85f5ff█&f███#85f5ff█   #85f5ff* #8c8c8cLiczba czlonkow wynosi " + ChatUtil.getValidGangColor(gang) + DataManager.getInstance().getLocal(null).getMembers(gang), "#85f5ff█&f█#85f5ff█&f█#85f5ff█", "");

                        return true;
                    }

                    final String gang = DataManager.getInstance().getLocal(player).getGang();

                    if (gang == null)
                    {
                        ChatManager.sendNotification(player, "Nie nalezysz do zadnego gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    ChatManager.sendMessages(player, "       ", "&f█████", "&f█#85f5ff█&f█#85f5ff█&f█   #85f5ff* #8c8c8cWyglad prefixu Twojego gangu to " + ChatUtil.getGangInChat(gang),
                            "&f█████   #85f5ff* #8c8c8cObecnym liderem jest " + ChatUtil.getValidGangColor(gang) + DataManager.getInstance().getLocal(null).getLider(gang), "#85f5ff█&f███#85f5ff█   #85f5ff* #8c8c8cLiczba czlonkow wynosi " + ChatUtil.getValidGangColor(gang) + DataManager.getInstance().getLocal(null).getMembers(gang), "#85f5ff█&f█#85f5ff█&f█#85f5ff█", "");

                    return true;
                }
                else if (args[0].equalsIgnoreCase("friendlyfire"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                    {
                        ChatManager.sendNotification(player, "Ziomek, Ty nawet nie masz gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (!DataManager.getInstance().getLocal(null).getLider(DataManager.getInstance().getLocal(player).getGang()).equalsIgnoreCase(player.getName()))
                    {
                        ChatManager.sendNotification(player, "Musisz byc liderem gangu, aby zmienic to ustawienie!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    DataManager.getInstance().getLocal(null).toggleFriendlyFire(DataManager.getInstance().getLocal(player).getGang());
                    ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cZmieniono ustawienie wzajemnego bicia na " + ChatUtil.isGangFriendlyFire(DataManager.getInstance().getLocal(player).getGang()) + "#8c8c8c!");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("wyrzuc"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                    {
                        ChatManager.sendNotification(player, "Ziomek, Ty nawet nie masz gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (!DataManager.getInstance().getLocal(null).getLider(DataManager.getInstance().getLocal(player).getGang()).equalsIgnoreCase(player.getName()))
                    {
                        ChatManager.sendNotification(player, "Wyjebac kogos moze tylko lider, sry!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (length < 2)
                    {
                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, ktorego chcesz wyjebac! Wzor komendy: #ffc936/gang wyrzuc <gracz>");
                        return true;
                    }

                    final String potencial_kicker = args[1];

                    for (Player kicker : Bukkit.getOnlinePlayers())
                        if (kicker.getName().equalsIgnoreCase(potencial_kicker))
                        {
                            if (!DataManager.getInstance().getLocal(kicker).getGang().equalsIgnoreCase(DataManager.getInstance().getLocal(player).getGang()))
                            {
                                ChatManager.sendNotification(player, "Ten gracz nawet nie jest w Twoim gangu!", ChatManager.NotificationType.ERROR);
                                return true;
                            }

                            DataManager.getInstance().getLocal(null).removePlayer(kicker);
                            ChatManager.sendMessage(kicker, Configuration.SERVER_FULL_PREFIX + "#fc7474Zostales wyrzucony ze swojego gangu!");
                            ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cPomyslnie wyrzucono gracza " + ChatUtil.returnPlayerColor(kicker) + kicker.getName() + "!");
                            ServerUtil.reloadContentsGlobal();
                            return true;
                        }

                    ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("ulepsz"))
                {
                    InventoryUtil.createNewInventory(player, 45, ChatColor.translateAlternateColorCodes('&', "&c&lSKLEP GANGU"), "sklep_gang");
                    return  true;
                }
                else if (args[0].equalsIgnoreCase("zapros"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                    {
                        ChatManager.sendNotification(player, "Ziomek, Ty nawet nie masz gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (length == 1)
                    {
                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, do ktorego chcesz wyslac zaproszenie do gangu! Wzor komendy: #ffc936/gang zapros <gracz>");
                        return true;
                    }

                    final String potencial_player = args[1];

                    for (Player dplayer : Bukkit.getOnlinePlayers())
                        if (dplayer.getName().equalsIgnoreCase(potencial_player))
                        {
                            if (DataManager.getInstance().getLocal(dplayer).getGang() != null)
                            {
                                ChatManager.sendNotification(player, "Ten gracz posiada juz swoj gang!", ChatManager.NotificationType.ERROR);
                                return true;
                            }

                            if (DataManager.getInstance().getLocal(null).getPlayerMembers(DataManager.getInstance().getLocal(player).getGang()).contains(dplayer.getName()))
                            {
                                ChatManager.sendNotification(player, "Ten gracz nalezy juz do Twoich szeregow w gangu!", ChatManager.NotificationType.ERROR);
                                return true;
                            }

                            setGang(player, dplayer);
                            setGang(dplayer, player);
                            ChatManager.sendMessage(player, "#f83044&l» &cWyslano zaproszenie do gangu do gracza " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + "!");
                            ChatManager.sendMessage(dplayer, "#f83044&l» &cOtrzymano zaproszenie do gangu od gracza " + ChatUtil.returnPlayerColor(player) + player.getName() + "!\n#fc7474&o/gang dolacz (gracz)&f&o - zaakceptowanie" + "\n#fc7474&o" + Configuration.SERVER_GANG_REQUEST_TIME + " sekund&f&o - czas oczekiwania");
                            VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#85f5ff★", 0, 20, 20);

                            new BukkitRunnable() { @Override public void run() {
                                setGang(player, null);
                            } }.runTaskLaterAsynchronously(Main.getInstance(), 20*Configuration.SERVER_GANG_REQUEST_TIME);
                            return true;
                        }

                    ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("dolacz"))
                {
                    if (length == 1)
                    {
                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWpisz nazwe gracza, od ktorego otrzymales zaproszenie do gangu! Wzor komendy: #ffc936/gang dolacz <gracz>");
                        return true;
                    }

                    final String potencial_player = args[1];

                    for (Player dplayer : Bukkit.getOnlinePlayers())
                        if (dplayer.getName().equalsIgnoreCase(potencial_player))
                        {
                            if (getGang(dplayer) == player)
                            {
                                ChatManager.sendMessage(dplayer, "#f83044&l» &cZaproszenie do gangu do gracza " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + "&c zostalo zaakceptowane!");
                                ChatManager.sendMessage(player, "#f83044&l» &cDolaczyles do gangu " + DataManager.getInstance().getLocal(dplayer).getGang() + "!");
                                setGang(dplayer, null);
                                setGang(player, null);
                                DataManager.getInstance().getLocal(null).addPlayerMembers(DataManager.getInstance().getLocal(dplayer).getGang(), player.getName());
                                return true;
                            }

                            ChatManager.sendNotification(player, "Miales gdzies dolaczyc? Moze czas minal, moze gracz wyslal juz zaproszenie komus innemu, ja nic nie wiem!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                    ChatManager.sendNotification(player, "Podany gracz nie jest on-line na serwerze!", ChatManager.NotificationType.ERROR);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("tpall"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                    {
                        ChatManager.sendNotification(player, "Ziomek, Ty nawet nie masz gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (!DataManager.getInstance().getLocal(null).getLider(DataManager.getInstance().getLocal(player).getGang()).equalsIgnoreCase(player.getName()))
                    {
                        ChatManager.sendNotification(player, "Tylko lider moze wywolac teleportacje wszystkich czlonkow!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja czlonkow gangu... #fff203⌛");

                    for (String member : DataManager.getInstance().getLocal(null).getPlayerMembers(DataManager.getInstance().getLocal(player).getGang()))
                        if (Bukkit.getPlayer(member) != null && Objects.requireNonNull(Bukkit.getPlayer(member)).isOnline())
                        {
                            Player dplayer = Bukkit.getPlayer(member);
                            assert dplayer != null;
                            dplayer.teleport(player);
                        }

                    return true;
                }
                else if (args[0].equalsIgnoreCase("baza"))
                {
                    if (DataManager.getInstance().getLocal(player).getGang() == null)
                    {
                        ChatManager.sendNotification(player, "Ziomek, Ty nawet nie masz gangu!", ChatManager.NotificationType.ERROR);
                        return true;
                    }

                    if (length == 1)
                    {
                        if (DataManager.getInstance().getLocal(null).getBase(DataManager.getInstance().getLocal(player).getGang()) == null)
                        {
                            ChatManager.sendNotification(player, "Twoj gang nie ma jeszcze ustawionej bazy!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja do bazy gangu... #fff203⌛");
                        player.teleport(DataManager.getInstance().getLocal(null).getBase(DataManager.getInstance().getLocal(player).getGang()));
                    }
                    else
                    {
                        if (!args[1].equalsIgnoreCase("ustaw"))
                        {
                            ChatManager.sendNotification(player, "Nie rozumiem ani slowa! Sprobuj uzyc #ffc936/gang baza ustaw #fc7474i bedzie dobrze!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        if (!DataManager.getInstance().getLocal(null).getLider(DataManager.getInstance().getLocal(player).getGang()).equalsIgnoreCase(player.getName()))
                        {
                            ChatManager.sendNotification(player, "Tylko lider moze ustawic lokalizacje bazy gangu!", ChatManager.NotificationType.ERROR);
                            return true;
                        }

                        DataManager.getInstance().getLocal(null).setBase(DataManager.getInstance().getLocal(player).getGang(), player.getLocation());
                        ChatManager.sendNotification(player, "#8c8c8cBaza Twojego gangu zostala zaktualizowana!", ChatManager.NotificationType.SUCCESS);
                    }
                    return true;
                }

                ChatManager.sendNotification(player, "Co kurwa?! Wpisz #ffc936/gang #fc7474i nie pierdol!", ChatManager.NotificationType.ERROR);
                return true;

            }
            return true;
        }

        return false;
    }
}