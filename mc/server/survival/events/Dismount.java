package mc.server.survival.events;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class Dismount implements Listener
{
    @EventHandler
    public void onEvent(EntityDismountEvent event)
    {
        if (event.getDismounted() instanceof ArmorStand)
        {
            event.getEntity().teleport(event.getEntity().getLocation().add(0, 1, 0));
            event.getDismounted().remove();
        }
    }
}