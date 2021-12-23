package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.DataManager;
import mc.server.survival.managers.FileManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.*;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityDamageByEntity 
implements Listener
{
	@EventHandler
	public void onEvent(EntityDamageByEntityEvent event) 
	{
		if (event.getEntity() instanceof Player)
		{
			final Player player = (Player) event.getEntity();

			if (player.getWorld().getName().equalsIgnoreCase("survival") && Configuration.SERVER_TERRAIN_PROTECTION)
				if (player.getLocation().distance(WorldUtil.SURVIVAL_SPAWN) < Configuration.SERVER_SPAWN_PROTECTION)
				{
					event.setCancelled(true);
					return;
				}

			if ((boolean) FileManager.getInstance().getConfigValue("visuals.critBlind"))
				if (event.getDamager().getFallDistance() > 0)
				{
					PacketPlayOutAnimation blind = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 2);
					PacketPlayOutAnimation particle = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 5);

					PacketUtil.sendPacket(player, blind);
					PacketUtil.sendPacket(player, particle);
				}

			event.setDamage(event.getDamage() / (0.0001 + NeuroManager.ABILITY_ENVIRONMENT_SENSITIVE.get(player)));
		}
		if (event.getDamager() instanceof Player) 
		{
			final Player player = (Player) event.getDamager();

			final double distance = player.getLocation().distance(event.getEntity().getLocation());
			final double Y = Math.abs(player.getLocation().getY() - event.getEntity().getLocation().getY());
			
			if (distance - Y > (getReach(player) + (event.getEntity().getWidth() / 2)) & event.getCause() == DamageCause.ENTITY_ATTACK)
			{
				event.setCancelled(true);
				return;
			}

			LivingEntity damagedEntity = (LivingEntity) event.getEntity();

			if (damagedEntity.getHealth() - event.getFinalDamage() <= 0)
			{
				if ((boolean) FileManager.getInstance().getConfigValue("visuals.blood"))
					player.getWorld().spawnParticle(Particle.BLOCK_CRACK, event.getEntity().getLocation().add(0, damagedEntity.getEyeHeight(), 0), 50, 0.02, 0.02, 0.02, Material.REDSTONE_BLOCK.createBlockData());

				if ((boolean) FileManager.getInstance().getConfigValue("visuals.damageIndicator"))
					NPCUtil.getInstance().createTempHologram(ColorUtil.formatHEX("&c-" + ((int) damagedEntity.getHealth()) + "❤"), damagedEntity.getLocation().add(0, damagedEntity.getEyeHeight(), 0));
			}
			else
				if ((boolean) FileManager.getInstance().getConfigValue("visuals.damageIndicator"))
					if (player.getFallDistance() <= 0)
						NPCUtil.getInstance().createTempHologram(ColorUtil.formatHEX("&c-" + ((int) event.getFinalDamage()) + "❤"), damagedEntity.getLocation().add(0, damagedEntity.getEyeHeight(), 0));
					else
						NPCUtil.getInstance().createTempHologram(ColorUtil.formatHEX("&c-" + ((int) event.getFinalDamage()) + "❤ &6(Critical)"), damagedEntity.getLocation().add(0, damagedEntity.getEyeHeight(), 0));

			new BukkitRunnable() { @Override public void run() {
                damagedEntity.setNoDamageTicks((int) FileManager.getInstance().getConfigValue("internal.hit-delay"));
            } }.runTaskLaterAsynchronously(Main.getInstance(), 1L);

            event.getEntity().getWorld().spawnParticle(Particle.SWEEP_ATTACK, event.getEntity().getLocation().add(0, 0.5, 0), 2, 0.5, 0.5, 0.5);
		}

		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
		{
			final Player attacker = (Player) event.getDamager();
			final Player victim = (Player) event.getEntity();

			if (DataManager.getInstance().getLocal(attacker).getGang() != null && DataManager.getInstance().getLocal(victim).getGang() != null)
				if (DataManager.getInstance().getLocal(attacker).getGang().equalsIgnoreCase(DataManager.getInstance().getLocal(victim).getGang()))
					if (!DataManager.getInstance().getLocal(attacker).getFriendlyFire(DataManager.getInstance().getLocal(attacker).getGang()))
					{
						event.setCancelled(true);
						return;
					}
		}

		if (event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))
			VelocityUtil.applyVelocity(event.getEntity(), (double) FileManager.getInstance().getConfigValue("internal.entity-knockback") *
				NeuroManager.ABILITY_COMBAT_KNOCKBACK.get(((Player) event.getDamager()).getPlayer()));
	}

	private double getReach(Player player)
	{
		final double reach = (double) FileManager.getInstance().getConfigValue("internal.hit-reach");

		return reach + NeuroManager.ABILITY_COMBAT_REACH.get(player);
	}
} 