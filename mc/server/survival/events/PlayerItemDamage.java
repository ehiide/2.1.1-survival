package mc.server.survival.events;

import mc.server.survival.managers.DataManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.MathUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class PlayerItemDamage implements Listener
{
    @EventHandler
    public void onEvent(PlayerItemDamageEvent event)
    {
        if (!NeuroManager.isDrugged(event.getPlayer())) return;

        final int noradrenaline = DataManager.getInstance().getLocal(event.getPlayer()).getNoradrenaline();

        if (noradrenaline > 30)
            event.setDamage(event.getDamage() * ((noradrenaline / 14)));

        if (MathUtil.chanceOf(NeuroManager.ABILITY_PLAYER_LUCK.get(event.getPlayer()) / 2))
            event.setDamage(0);
    }
}