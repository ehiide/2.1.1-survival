package mc.server.survival.utils;

import mc.server.survival.files.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class VisualUtil 
{
	public static void spawnFirework(final Location location)
	{
		Firework firework = (Firework) location.getWorld().spawn(location, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		
		meta.addEffects(FireworkEffect.builder().flicker(true).withColor(Color.RED).trail(true).with(Type.STAR).withFade(Color.WHITE).build());
		meta.setPower(1);
		firework.setFireworkMeta(meta);
	}

	public static void showActionbar(final Player player, String message)
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("" + ColorUtil.formatHEX(message)));
	}
	
	@SuppressWarnings("deprecation")
	public static void showTitle(final Player player, String top, String bottom)
	{
		player.sendTitle(ChatColor.translateAlternateColorCodes('&', top), ChatColor.translateAlternateColorCodes('&', bottom));
	}
	
	public static void showDelayedTitle(final Player player, String top, String bottom, final int start, final int time, final int end)
	{
		player.sendTitle(ColorUtil.formatHEX("&7" + bottom), ColorUtil.formatHEX("&7" + top), start, time, end);
	}

	public static void showServerChangeTitle(@NotNull final Player player)
	{
		String worldName = player.getWorld().getName().toUpperCase();

		if (worldName.equalsIgnoreCase("survival_nether".toUpperCase()) || worldName.equalsIgnoreCase("survival_the_end".toUpperCase()))
			worldName = worldName.substring(9);

		worldName = worldName.replace("_", " ");
		int worldNameLength = worldName.length();

		for (int x = 0; x <= worldNameLength; x++)
		{
			String factor = worldName.substring(0, x);

			new BukkitRunnable() { @Override public void run() {
				showDelayedTitle(player, "", "&a&l✦ #f83044&l" + factor + " &a&l✦", 0, 10, 0);
			} }.runTaskLaterAsynchronously(Main.getInstance(), 20L + (long) x);
		}

		for (int x = 0; x <= worldNameLength; x++)
		{
			final String factor = worldName.substring(0, x);
			final String factor2 = worldName.substring(x, worldNameLength);

			new BukkitRunnable() { @Override public void run() {
				showDelayedTitle(player, "", "&a&l✦ #fff203&l" + factor + "#f83044&l" + factor2 + " &a&l✦", 0, 10, 0);
			} }.runTaskLaterAsynchronously(Main.getInstance(), 21L + (long) worldNameLength + (long) x);
		}

		for (int x = 0; x <= worldNameLength; x++)
		{
			final String factor = worldName.substring(0, x);
			final String factor2 = worldName.substring(x, worldNameLength);

			new BukkitRunnable() { @Override public void run() {
				showDelayedTitle(player, "", "&a&l✦ #f83044&l" + factor + "#fff203&l" + factor2 + " &a&l✦", 0, 10, 0);
			} }.runTaskLaterAsynchronously(Main.getInstance(), 22L + (long) worldNameLength * 2 + (long) x);
		}

		for (int x = worldNameLength; x >= 0; x--)
		{
			final String factor = worldName.substring(0, x);

			new BukkitRunnable() { @Override public void run() {
				showDelayedTitle(player, "", "&a&l✦ #f83044&l" + factor + " &a&l✦", 0, 3, 0);
			} }.runTaskLaterAsynchronously(Main.getInstance(), 23L + (long) worldNameLength * 3 + (long) (worldNameLength - x));
		}
	}

	public static void simulateFakeExplosion(final Player player, final int delay)
	{
		new BukkitRunnable() { @Override public void run() {
			player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation().add(0, 1, 0), 1, 15, 2, 15);
		} }.runTaskLater(Main.getInstance(), 20L * delay);
	}

	public static void showPlayerParticle(final Player player, final Particle particle)
	{
		player.spawnParticle(particle, player.getLocation().add(0, 0.3, 0), 30, 0.1, 0.1, 0.1);
	}
}