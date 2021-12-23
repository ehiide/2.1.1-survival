package mc.server.survival.events;

import mc.server.survival.managers.NeuroManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener
{
    @EventHandler
    public void onEvent(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
           final Player player = (Player) event.getEntity();
           final EntityDamageEvent.DamageCause damageCause = event.getCause();

            if (damageCause.equals(EntityDamageEvent.DamageCause.LAVA) ||
                    damageCause.equals(EntityDamageEvent.DamageCause.FIRE) ||
                    damageCause.equals(EntityDamageEvent.DamageCause.FIRE_TICK))
                if (NeuroManager.ABILITY_ENVIRONMENT_FIRE_RESISTANCE.get(player))
                    event.setCancelled(true);

            if (damageCause.equals(EntityDamageEvent.DamageCause.CONTACT))
                event.setDamage(event.getDamage() / (0.0001 + NeuroManager.ABILITY_ENVIRONMENT_SENSITIVE.get(player)));
        }
    }
}