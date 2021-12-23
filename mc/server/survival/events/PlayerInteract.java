package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.items.Chemistries;
import mc.server.survival.items.ChemistryDrug;
import mc.server.survival.items.ChemistryItem;
import mc.server.survival.items.InternalItem;
import mc.server.survival.managers.FileManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.*;
import org.bukkit.*;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class PlayerInteract 
implements Listener
{
	public static HashMap<Player, ClickSequence> sequence_map = new HashMap<>();
	public static HashMap<Player, ClickSequence> lastSequence_map = new HashMap<>();

	public static PlayerInteract.ClickSequence getSequence(Player player)
	{
		return sequence_map.get(player);
	}

	public static PlayerInteract.ClickSequence getLastSequence(Player player)
	{
		return lastSequence_map.get(player);
	}

	public static void setSequence(Player player, PlayerInteract.ClickSequence sequence)
	{
		sequence_map.put(player, sequence);
	}

	public static void setLastSequence(Player player, PlayerInteract.ClickSequence lastSequence)
	{
		lastSequence_map.put(player, lastSequence);
	}

	@EventHandler
	public void onEvent(PlayerInteractEvent event)
	{
        Player player = event.getPlayer();
	    
		if (event.getAction() == Action.PHYSICAL && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.FARMLAND)
			if ((boolean) FileManager.getInstance().getConfigValue("function.plant-protection.status"))
				event.setCancelled(true);

		if (player.getInventory().getItemInMainHand().isSimilar(new InternalItem().get("magic_rod")))
			if (!player.hasCooldown(Material.STONE_HOE))
            {
                if (getSequence(player) == null)
				{
                    final ClickSequence clickSequence = new ClickSequence(ClickType.EMPTY, ClickType.EMPTY, ClickType.EMPTY);
                    setSequence(player, clickSequence);
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR)
				{
					ClickSequence clickSequence = getSequence(player);
					clickSequence.addSequence(ClickType.RIGHT);
					setSequence(player, clickSequence);

				}
				else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                {
					ClickSequence clickSequence = getSequence(player);
					clickSequence.addSequence(ClickType.LEFT);
					setSequence(player, clickSequence);
				}

				if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK ||
						event.getAction() == Action.RIGHT_CLICK_AIR)
				{
					if (getSequence(player).isDone())
					{
						if (getSequence(player).toString().equalsIgnoreCase("RIGHTRIGHTRIGHT"))
						{
							player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 0.5, 0), 100);
							player.setHealth(Math.min(player.getHealth() + 10, player.getMaxHealth()));
							player.setFoodLevel(24);
							player.setSaturation(6);
							player.setSaturatedRegenRate(80);
						}
						else if (getSequence(player).toString().equalsIgnoreCase("LEFTLEFTLEFT"))
						{
							VelocityUtil.applyDirectionableVelocity(player, 15);
						}
						else if (getSequence(player).toString().equalsIgnoreCase("RIGHTLEFTRIGHT"))
						{
							Entity entity = player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.FIREBALL);
							Fireball fireball = (Fireball) entity;
							fireball.setDirection(player.getEyeLocation().getDirection());

							new BukkitRunnable() { @Override public void run() {
								if (fireball.isDead())
									this.cancel();
								else
									fireball.getWorld().spawnParticle(Particle.FLAME, fireball.getLocation(), 4);
							} }.runTaskTimerAsynchronously(Main.getInstance(), 2, 2);

						}
						else if (getSequence(player).toString().equalsIgnoreCase("LEFTRIGHTLEFT"))
						{
							player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation().add(0, 0.5, 0), 100);
							Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
								if (!onlinePlayer.getName().equalsIgnoreCase(player.getName()))
								{
									onlinePlayer.hidePlayer(Main.getInstance(), player);

									new BukkitRunnable() { @Override public void run() {
										onlinePlayer.showPlayer(Main.getInstance(), player);
									} }.runTaskLater(Main.getInstance(), 100);
								}
							});
						}
						else
							new BukkitRunnable() { @Override public void run() {
								VisualUtil.showDelayedTitle(player, "#fc7474✖", "", 0, 20, 0);
							} }.runTaskLaterAsynchronously(Main.getInstance(), 20);

						SoundUtil.playPlayerSound(player, Sound.BLOCK_ANCIENT_DEBRIS_FALL, 1, 7);
						player.setCooldown(Material.STONE_HOE, 180);
					}

					setLastSequence(player, getSequence(player));

					String suquenceString = getSequence(player).toString();
					suquenceString = suquenceString.replaceAll("RIGHT", " &cPPM &7+");
					suquenceString = suquenceString.replaceAll("LEFT", " &cLPM &7+");
					suquenceString = suquenceString.replaceAll("EMPTY", " &e? &7+");
					suquenceString = suquenceString.substring(1, suquenceString.length() - 4);

					VisualUtil.showDelayedTitle(player, "" + suquenceString, "&7", 0, 20, 0);

					final ClickSequence clickSequence = new ClickSequence(ClickType.EMPTY, ClickType.EMPTY, ClickType.EMPTY);

					new BukkitRunnable() { @Override public void run() {
						if (getSequence(player).toString().equalsIgnoreCase(getLastSequence(player).toString()))
							setSequence(player, clickSequence);
					} }.runTaskLaterAsynchronously(Main.getInstance(), 40);
				}
            }

		if ((boolean) FileManager.getInstance().getConfigValue("function.sitting.status"))
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().contains("STAIRS"))
				{
					if (player.getWorld().getName().equalsIgnoreCase("survival") && Configuration.SERVER_TERRAIN_PROTECTION)
						event.setCancelled(true);

					if (!event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType().isAir()) return;

					if (player.getVehicle() != null) return;

					if (player.isSneaking()) return;

					if ((boolean) FileManager.getInstance().getConfigValue("function.sitting.height-fix"))
						if (Math.abs(player.getLocation().getBlockY() - event.getClickedBlock().getLocation().getBlockY()) > 1.5)
							return;

					if (player.getInventory().getItemInMainHand().getType()!= Material.AIR
							|| player.getInventory().getItemInOffHand().getType() != Material.AIR) return;

					if (event.getClickedBlock().getType().toString().contains("STAIRS"))
					{
						Stairs stairs = (Stairs) event.getClickedBlock().getBlockData();

						if (stairs.getHalf() == Bisected.Half.TOP) return;

						if (stairs.getShape() != Stairs.Shape.STRAIGHT) return;

						if (NPCUtil.getChairRelative(stairs) == 404) return;

						ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(player.getWorld()).spawnEntity(new Location(event.getClickedBlock().getWorld(),
								event.getClickedBlock().getBoundingBox().getCenter().getX(),
								event.getClickedBlock().getBoundingBox().getCenter().getY() - 0.2,
								event.getClickedBlock().getBoundingBox().getCenter().getZ(),
										NPCUtil.getChairRelative(stairs),
										0),
								EntityType.ARMOR_STAND);

						player.teleport(new Location(event.getClickedBlock().getWorld(),
								event.getClickedBlock().getBoundingBox().getCenter().getX(),
								event.getClickedBlock().getBoundingBox().getCenter().getY() - 0.2,
								event.getClickedBlock().getBoundingBox().getCenter().getZ(),
										NPCUtil.getChairRelative(stairs),
										0));

						armorStand.setCustomName("CHAIR");
						armorStand.setCustomNameVisible(false);
						armorStand.setMarker(true);
						armorStand.setInvulnerable(true);
						armorStand.setCollidable(false);
						armorStand.setGravity(false);
						armorStand.setRemoveWhenFarAway(false);
						armorStand.setBasePlate(false);
						armorStand.setArms(true);
						armorStand.setVisible(false);
						armorStand.setPassenger(player);
					}
				}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if (event.getClickedBlock() != null)
				if (event.getClickedBlock().getType() == Material.BREWING_STAND)
				{
					if (player.getInventory().getItemInMainHand().getType() == Material.AIR
							&& player.getInventory().getItemInOffHand().getType() == Material.AIR)
						if (event.getClickedBlock().getLocation().add(0, -1, 0).getBlock().getType() == Material.LAVA_CAULDRON)
						{
							event.setCancelled(true);
							InventoryUtil.createNewInventory(player, 54, ChatColor.translateAlternateColorCodes('&', "&c&lSTATYW ALCHEMICZNY &0&1"), "drug_table_amina");
						}
				}

		if (player.getWorld().getName().equalsIgnoreCase("survival") && Configuration.SERVER_TERRAIN_PROTECTION)
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getLocation().distance(WorldUtil.SURVIVAL_SPAWN) < Configuration.SERVER_SPAWN_PROTECTION)
				{
					event.setCancelled(true);
					return;
				}

		if (player.getWorld().getName().equalsIgnoreCase("survival"))
			if (player.getItemInHand().isSimilar(new ItemStack(Material.ENDER_PEARL)) || player.getItemInHand().isSimilar(new ItemStack(Material.CHORUS_FRUIT)))
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
					if (player.getLocation().distance(WorldUtil.SURVIVAL_SPAWN) < 256)
					{
						event.setCancelled(true);
						return;
					}

		if (event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			if (event.getItem() == null) return;

			if (player.getInventory().getItemInOffHand().getType() != Material.AIR) return;

			boolean isKnownDrug = Chemistries.getInstance().isKnown(player.getInventory().getItemInMainHand());
			boolean isNotOnCooldown = !player.hasCooldown(Material.SUGAR) && !player.hasCooldown(Material.GUNPOWDER);
			boolean haveDrug = event.getItem().getType() == Material.SUGAR || event.getItem().getType() == Material.GUNPOWDER;

			if (isKnownDrug && isNotOnCooldown && haveDrug)
			{
				String itemName = event.getItem().hasItemMeta() ? event.getItem().getItemMeta().getDisplayName() : null;
				ChemistryItem chemistryItem = Chemistries.getInstance().byName(itemName);
				ItemStack itemStack = ChemistryDrug.getDrug(chemistryItem);

				Inventory.removeItem(player, itemStack, 1);

				if (chemistryItem.getAffinity().isOpioidic())
					NeuroManager.normalize(player,
						chemistryItem.getAffinity().getOpioidic());
				else if (chemistryItem.getAffinity().isAmine())
					NeuroManager.modify(player,
						chemistryItem.getAffinity().getSerotonine(),
						chemistryItem.getAffinity().getDopamine(),
						chemistryItem.getAffinity().getNoradrenaline(),
						chemistryItem.getAffinity().getGABA());

				player.setCooldown(Material.SUGAR, 120); player.setCooldown(Material.GUNPOWDER, 120);
			}
		}
	}

	public enum ClickType
	{
		RIGHT, LEFT, EMPTY
	}

	public class ClickSequence
	{
		private ClickType firstClick, secondClick, finalClick;

		public ClickSequence(ClickType firstClick, ClickType secondClick, ClickType finalClick)
		{
			this.firstClick = firstClick;
			this.secondClick = secondClick;
			this.finalClick = finalClick;
		}

		public ClickSequence addSequence(ClickType clickType)
		{
			if (firstClick == ClickType.EMPTY)
			{
				this.firstClick = clickType;
				return new ClickSequence(this.firstClick, this.secondClick, this.finalClick);
			}

			if (secondClick == ClickType.EMPTY)
			{
				this.secondClick = clickType;
                return new ClickSequence(this.firstClick, this.secondClick, this.finalClick);
			}

			if (finalClick == ClickType.EMPTY)
			{
				this.finalClick = clickType;
                return new ClickSequence(this.firstClick, this.secondClick, this.finalClick);
			}

			resetSequence();
			this.firstClick = clickType;

            return new ClickSequence(this.firstClick, this.secondClick, this.finalClick);
		}

		public boolean isDone() { return this.finalClick != ClickType.EMPTY; }

		public String toString() { return this.firstClick.toString() + this.secondClick.toString() + this.finalClick.toString(); }

		public void resetSequence()
		{
			this.firstClick = ClickType.EMPTY;
			this.secondClick = ClickType.EMPTY;
			this.finalClick = ClickType.EMPTY;
		}
	}
}