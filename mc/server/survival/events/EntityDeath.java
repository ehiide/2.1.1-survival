package mc.server.survival.events;

import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.QuestUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener
{
    @EventHandler
    public void onEvent(EntityDeathEvent event)
    {
        if (!(event.getEntity() instanceof Player) && event.getEntity().getKiller() != null)
        {
            final int serotonine = DataManager.getInstance().getLocal(event.getEntity().getKiller()).getSerotonine();
            int xp = event.getDroppedExp();

            if (serotonine >= 60)
                xp = xp * 2;

            if (serotonine <= -20)
                xp = 0;

            if (serotonine <= -40)
                event.getDrops().clear();

            event.setDroppedExp(xp + (DataManager.getInstance().getLocal(event.getEntity().getKiller()).getUpgradeLevel(event.getEntity().getKiller().getName(), "thiefy")));
            QuestUtil.manageQuest(event.getEntity().getKiller(), 5);
        }
    }
}