package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.items.Chemistries;
import mc.server.survival.items.ChemistryDrug;
import mc.server.survival.items.InternalItem;
import mc.server.survival.managers.DataManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ServerSide
implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("world")) {
                player.teleport(new Location(Bukkit.getWorld(args[1].toString()), 0, 90, 0, 0, 0));
                return true;
            }
            else if (args[0].equalsIgnoreCase("tp")) {
                player.teleport(new Location(player.getWorld(), (double) Integer.parseInt(args[1].toString()), Integer.parseInt(args[2].toString()), Integer.parseInt(args[3].toString()), 0, 0));
                return true;
            }
            else if (args[0].equalsIgnoreCase("gm")) {
                player.setGameMode(args[1].equalsIgnoreCase("0") ? GameMode.SURVIVAL : GameMode.CREATIVE);
                return true;
            }
            else if (args[0].equalsIgnoreCase("iitem"))
            {
                String itemName = args[1].toString();
                player.getInventory().addItem(new InternalItem().get(itemName));
                return true;
            }
            else if (args[0].equalsIgnoreCase("item"))
            {
                String itemName = args[1];
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.getInstance().byName(itemName)));
                return true;
            }
            else if (args[0].equalsIgnoreCase("rl")) {
                Bukkit.reload();
                player.sendMessage("Reloaded.");
                return true;
            }
            else if (args[0].equalsIgnoreCase("gui")) {
                InventoryUtil.createNewInventory(player, 54, "unhandled gui snapshot", args[1].toString());
                return true;
            }
            else if (args[0].equalsIgnoreCase("money")) {
                DataManager.getInstance().getLocal(player).addMoney(Integer.parseInt(args[1]));
                return true;
            }
            else if (args[0].equalsIgnoreCase("chem-clear")) {
                NeuroManager.set(player, 0, 0, 0 ,0);
                return true;
            }
            else if (args[0].equalsIgnoreCase("chem-set")) {
                NeuroManager.set(player, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]) ,Integer.parseInt(args[4]));
                return true;
            }
            else if (args[0].equalsIgnoreCase("protection")) {
                if (Configuration.SERVER_TERRAIN_PROTECTION)
                    Configuration.SERVER_TERRAIN_PROTECTION = false;
                else
                    Configuration.SERVER_TERRAIN_PROTECTION = true;
                return true;
            }
        }

        return false;
    }
}