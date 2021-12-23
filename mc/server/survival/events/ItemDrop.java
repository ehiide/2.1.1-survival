package mc.server.survival.events;

import mc.server.survival.managers.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Objects;

public class ItemDrop implements Listener
{
    public void onEvent(PlayerDropItemEvent event)
    {
        if (!(boolean) FileManager.getInstance().getConfigValue("visuals.items"))
            return;

        Entity entity = event.getItemDrop();

        if (!Objects.requireNonNull(event.getItemDrop().getItemStack().getItemMeta()).getDisplayName().equalsIgnoreCase(""))
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&a" + Objects.requireNonNull(event.getItemDrop().getItemStack().getItemMeta()).getDisplayName() + " &ex" + event.getItemDrop().getItemStack().getAmount()));
        else
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&a" + event.getItemDrop().getName() + " &ex" + event.getItemDrop().getItemStack().getAmount()));
        entity.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEvent(ItemSpawnEvent event)
    {
        if (!(boolean) FileManager.getInstance().getConfigValue("visuals.items"))
            return;

        Entity entity = event.getEntity();

        if (!Objects.requireNonNull(event.getEntity().getItemStack().getItemMeta()).getDisplayName().equalsIgnoreCase(""))
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&a" + Objects.requireNonNull(event.getEntity().getItemStack().getItemMeta()).getDisplayName() + " &ex" + event.getEntity().getItemStack().getAmount()));
        else
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&a" + event.getEntity().getName() + " &ex" + event.getEntity().getItemStack().getAmount()));
        entity.setCustomNameVisible(true);
    }
}