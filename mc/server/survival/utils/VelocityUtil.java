package mc.server.survival.utils;

import mc.server.survival.files.Main;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VelocityUtil 
{
	public static void resetVelocity(@NotNull Entity entity)
	{
		entity.setVelocity(new Vector(0, 0, 0));
	}
	
	public static void convertVelocity(@NotNull Entity entity, final double X, final double Y, final double Z)
	{
		entity.setVelocity(new Vector(X, Y, Z));
	}
	
	public static void applyVelocity(@NotNull Entity entity, final double strength)
	{
		new BukkitRunnable() 
		{
			@Override
			public void run()
			{
				double motX = entity.getVelocity().getX() * strength;
				double motZ = entity.getVelocity().getZ() * strength;
				double motY = entity.getVelocity().getY();
				resetVelocity(entity);
				convertVelocity(entity, motX, motY, motZ);
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 1L);
	}

	public static void applyDirectionableVelocity(@NotNull Entity entity, final double strength)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				double motX = entity.getLocation().getDirection().getX() * strength;
				double motZ = entity.getLocation().getDirection().getZ() * strength;
				double motY = entity.getLocation().getDirection().getY() * strength;
				resetVelocity(entity);
				convertVelocity(entity, motX, motY, motZ);
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 1L);
	}
}