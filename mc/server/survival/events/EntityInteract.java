package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EntityInteract implements Listener
{
    @EventHandler
    public void onEvent(PlayerInteractEntityEvent event)
    {
        final Location location = event.getRightClicked().getLocation();

        if (location.equals(new Location(WorldUtil.getWorld("survival"), 453.5, 65, 9.5, 90, 0)))
        {
            event.setCancelled(true);
            InventoryUtil.createNewInventory(event.getPlayer(), 45, ChatColor.translateAlternateColorCodes('&', "&c&lSKLEP"), "sklep");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("survival"), 446.5, 65, 14.5, 180, 0)))
        {
            event.setCancelled(true);
            WorldUtil.queueWorldChange(event.getPlayer(), WorldUtil.WorldType.SURVIVAL);
        }
        else if (location.equals(new Location(WorldUtil.getWorld("survival"), 449.5, 65, 14.5, 180, 0)))
        {
            event.setCancelled(true);
            WorldUtil.teleportRandomly(event.getPlayer());
        }
        else if (location.equals(new Location(WorldUtil.getWorld("survival"), 437.5, 65, 14.5, 180, 0)))
        {
            event.setCancelled(true);
            ChatManager.sendMessage(event.getPlayer(), Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa otwieranie menu postaci... #fff203⌛");
            InventoryUtil.createNewInventory(event.getPlayer(), 54, ChatColor.translateAlternateColorCodes('&', "&c&lPOSTAC"), "postac");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("survival"), 401.5, 76, 17.5, 155, 0)))
        {
            event.setCancelled(true);
            InventoryUtil.createNewInventory(event.getPlayer(), 45, ChatColor.translateAlternateColorCodes('&', "&c&lQUESTY"), "questy");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("survival"), 430.5, 65, 21.5, 180, 0)))
        {
            event.setCancelled(true);

            long time = Objects.requireNonNull(WorldUtil.getWorld("survival")).getTime();

            if (time > 14000 && time < 24000)
                InventoryUtil.createNewInventory(event.getPlayer(), 36, ChatColor.translateAlternateColorCodes('&', "&c&lMONOPOLOWY U STASIA"), "monopolowy");
            else
                ChatManager.sendMessage(event.getPlayer(), Configuration.SERVER_FULL_PREFIX + "#fc7474Wypierdalaj menelu! Moj sklep jest czynny tylko w nocnych godzinach!");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("survival"), 404.5, 65, 3.5, -90, 0)))
        {
            event.setCancelled(true);

            final ItemStack i = event.getPlayer().getInventory().getItemInMainHand();

            if (i.getType().toString().contains("SKULL") || i.getType().toString().contains("HEAD"))
            {
                Inventory.removeItem(event.getPlayer(), i, 1);
                DataManager.getInstance().getLocal(event.getPlayer()).addMoney(50);
                ServerUtil.reloadContents(event.getPlayer());
            }
            else
                ChatManager.sendMessage(event.getPlayer(), Configuration.SERVER_FULL_PREFIX + "#fc7474Jak Ci zara przypierdole! Ja jestem od sprzedawania glow graczy i mobow, a nie od klikania! Won!");
        }

        if (event.getRightClicked() instanceof Player && DataManager.getInstance().getLocal(event.getPlayer()).getMarry() != null && event.getRightClicked().equals(Bukkit.getPlayer(DataManager.getInstance().getLocal(event.getPlayer()).getMarry())))
            if (event.getRightClicked().getName().equalsIgnoreCase(DataManager.getInstance().getLocal(event.getPlayer()).getMarry()))
                if (location.distance(event.getPlayer().getLocation()) < 1.5)
                {
                    final int timexp = TimeUtil.getDifferenceInMinutes(DataManager.getInstance().getLocal(event.getPlayer()).getMarryDate()) / 6;
                    final int xp = DataManager.getInstance().getLocal(event.getPlayer()).getMarryLevel() + DataManager.getInstance().getLocal(null).getMarryLevel(DataManager.getInstance().getLocal(event.getPlayer()).getMarry()) + timexp;
                    final int level = xp / 100;

                    if (level >= 100)
                        QuestUtil.manageQuest(event.getPlayer(), 12);

                    event.getPlayer().getWorld().spawnParticle(Particle.HEART, location.add(0, 2, 0), 1, 1, 1, 1, 1);
                    if (MathUtil.chanceOf(12))
                        DataManager.getInstance().getLocal(event.getPlayer()).setMarryLevel(DataManager.getInstance().getLocal(event.getPlayer()).getMarryLevel() + 1);
                }

        if (event.getRightClicked() instanceof Tameable)
        {
            final Tameable tameable = (Tameable) event.getRightClicked();

            if (!tameable.isTamed())
                if (MathUtil.chanceOf(NeuroManager.ABILITY_PLAYER_LUCK.get(event.getPlayer())))
                    new EntityTameEvent(tameable, event.getPlayer());
        }
    }
}