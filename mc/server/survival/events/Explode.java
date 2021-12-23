package mc.server.survival.events;

import mc.server.survival.managers.FileManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class Explode implements Listener
{
    @EventHandler
    public void onEvent(EntityExplodeEvent event)
    {
        if (!(boolean) FileManager.getInstance().getConfigValue("visuals.explode.status"))
            return;

        final Entity entity = event.getEntity();
        final World world = entity.getWorld();

        if (entity instanceof TNTPrimed)
        {
            world.spawnParticle(Particle.SMOKE_LARGE, event.getEntity().getLocation().add(0, 0.25, 0), 35, 5, 5, 5);

            final Material material = event.getLocation().add(0, -1, 0).getBlock().getType();
            final Location location = event.getLocation().add(0, 1, 0);

            for (int x = 0; x < (int) FileManager.getInstance().getConfigValue("visuals.explode.block-count"); x++)
            {
                FallingBlock fallingBlock = world.spawnFallingBlock(location, material.createBlockData());
                fallingBlock.setDropItem(false);
                fallingBlock.setHurtEntities(false);
                fallingBlock.setVelocity(new Vector(new Random().nextDouble() - 0.5, (new Random().nextDouble() + 0.45) / 2, new Random().nextDouble() - 0.5));
            }
        }
    }
}