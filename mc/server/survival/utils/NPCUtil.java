package mc.server.survival.utils;

import mc.server.survival.files.Main;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class NPCUtil
{
    private NPCUtil() {}

    static NPCUtil instance = new NPCUtil();

    public static NPCUtil getInstance()
    {
        return instance;
    }

    public void reloadEntities()
    {
        removeEntities();
        createEntities();
    }

    public void createEntity(EntityType entity, String name, Location location)
    {
       LivingEntity npc = (LivingEntity) Objects.requireNonNull(Bukkit.getWorld("survival")).spawnEntity(location, entity);

       npc.setCustomName(name);
       npc.setCustomNameVisible(false);

       npc.setCollidable(false);
       npc.setAI(false);
       npc.setInvulnerable(true);
       npc.setGravity(false);
       npc.setRemoveWhenFarAway(false);

       if (entity.equals(EntityType.VILLAGER))
       {
           Villager v = (Villager) npc;
           v.setProfession(Villager.Profession.WEAPONSMITH);
           v.getEquipment().setItemInMainHand(new ItemStack(Material.EMERALD));
       }
       else if (entity.equals(EntityType.WITHER_SKELETON))
       {
           WitherSkeleton w = (WitherSkeleton) npc;
           Objects.requireNonNull(w.getEquipment()).setItemInOffHand(new ItemStack(Material.SHIELD));
           ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
           ItemMeta meta = helmet.getItemMeta();
           assert meta != null;
           meta.setUnbreakable(true);
           helmet.setItemMeta(meta);
           w.getEquipment().setHelmet(helmet);
       }
    }

    public void createHologram(String text, Location location)
    {
        ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);

        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setCollidable(false);
        armorStand.setGravity(false);
        armorStand.setRemoveWhenFarAway(false);

        armorStand.setVisible(false);
    }

    public void createTempHologram(String text, Location location)
    {
        ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location.add(0, 200 ,0), EntityType.ARMOR_STAND);

        armorStand.setSmall(true);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setCollidable(false);
        armorStand.setInvisible(true);

        new BukkitRunnable() { @Override public void run() {
            armorStand.teleport(location.add(0, -200, 0));
        } }.runTaskLater(Main.getInstance(), 2L);

        new BukkitRunnable() { @Override public void run() {
            removeEntity(armorStand);
        } }.runTaskLater(Main.getInstance(), 20L);
    }

    public void createEntities()
    {
        createEntity(EntityType.VILLAGER, "   ", new Location(Bukkit.getWorld("survival"), 453.5, 65, 9.5, 90, 0));
        createEntity(EntityType.VILLAGER, "   ", new Location(Bukkit.getWorld("survival"), 446.5, 65, 14.5, 180, 0));
        createEntity(EntityType.VILLAGER, "   ", new Location(Bukkit.getWorld("survival"), 449.5, 65, 14.5, 180, 0));
        createEntity(EntityType.VILLAGER, "   ", new Location(Bukkit.getWorld("survival"), 401.5, 76, 17.5, 155, 0));
        createEntity(EntityType.VILLAGER, "   ", new Location(Bukkit.getWorld("survival"), 430.5, 65, 21.5, 180, 0));
        createEntity(EntityType.WITHER_SKELETON, "   ", new Location(Bukkit.getWorld("survival"), 404.5, 65, 3.5, -90, 0));
        createEntity(EntityType.VILLAGER, "   ", new Location(Bukkit.getWorld("survival"), 437.5, 65, 14.5, 180, 0));

        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lPRZEDMIOTY, DODATKI & EFEKTY"), new Location(Bukkit.getWorld("survival"), 453.5, 67.25, 9.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby otworzyc sklep"), new Location(Bukkit.getWorld("survival"), 453.5, 67, 9.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lTELEPORTER SWIATOWY"), new Location(Bukkit.getWorld("survival"), 449.5, 67.25, 14.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby sie przeteleportowac"), new Location(Bukkit.getWorld("survival"), 449.5, 67, 14.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lTELEPORTER MIEDZYSWIATOWY"), new Location(Bukkit.getWorld("survival"), 446.5, 67.25, 14.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby sie przeteleportowac"), new Location(Bukkit.getWorld("survival"), 446.5, 67, 14.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lTWOJA POSTAC & ULEPSZENIA"), new Location(Bukkit.getWorld("survival"), 437.5, 67.25, 14.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby poznac, zmienic samego siebie"), new Location(Bukkit.getWorld("survival"), 437.5, 67, 14.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lQUESTY I ZLECENIA"), new Location(Bukkit.getWorld("survival"), 401.5, 78.25, 17.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby wypelnic serwerowe zadania"), new Location(Bukkit.getWorld("survival"), 401.5, 78, 17.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lLOWCA GLOW"), new Location(Bukkit.getWorld("survival"), 404.5, 67.75, 3.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby sprzedac glowe ofiary"), new Location(Bukkit.getWorld("survival"), 404.5, 67.5, 3.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&e&lMONOPOLOWY U STASIA"), new Location(Bukkit.getWorld("survival"), 430.5, 67.5, 21.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby otworzyc spizarnie"), new Location(Bukkit.getWorld("survival"), 430.5, 67.25, 21.5));
        createHologram(ChatColor.translateAlternateColorCodes('&', "&c&o(SKLEP CZYNNY OD 20:00 DO 6:00)"), new Location(Bukkit.getWorld("survival"), 430.5, 67, 21.5));
    }

    public void removeEntities()
    {
        final String[] entitiesName = {ChatColor.translateAlternateColorCodes('&', "&e&lPRZEDMIOTY, DODATKI & EFEKTY"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby otworzyc sklep"),
                                 ChatColor.translateAlternateColorCodes('&', "&e&lTELEPORTER SWIATOWY"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby otworzyc menu"),
                                 ChatColor.translateAlternateColorCodes('&', "&e&lTELEPORTER MIEDZYSWIATOWY"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby sie przeteleportowac"),
                                 ChatColor.translateAlternateColorCodes('&', "&e&lTWOJA POSTAC & ULEPSZENIA"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby poznac, zmienic samego siebie"),
                                 ChatColor.translateAlternateColorCodes('&', "&e&lQUESTY I ZLECENIA"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby wypelnic serwerowe zadania"),
                                 ChatColor.translateAlternateColorCodes('&', "&e&lLOWCA GLOW"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby sprzedac glowe ofiary"),
                                 ChatColor.translateAlternateColorCodes('&', "&e&lMONOPOLOWY U STASIA"),
                                 ChatColor.translateAlternateColorCodes('&', "&7Kliknij &cPPM &7aby otworzyc spizarnie"),
                                 ChatColor.translateAlternateColorCodes('&', "&c&o(SKLEP CZYNNY OD 20:00 DO 6:00)"),
                                 "   ", "&fFinal hit!", "CHAIR"};

        for (World world : WorldUtil.getWorlds())
            for (Entity entity : world.getEntities())
                for (String name : entitiesName)
                {
                    if (entity.getName().equalsIgnoreCase(name) && Bukkit.getPlayer(entity.getName()) == null)
                        removeEntity(entity);

                    if (entity.getName().contains("-"))
                        removeEntity(entity);
                }
    }

    public void removeEntity(Entity entity)
    {
        entity.remove();
    }

    public static float getChairRelative(Stairs stairs)
    {
        if (stairs.getFacing() == BlockFace.WEST) return -90;
        else if (stairs.getFacing() == BlockFace.EAST) return 90;
        else if (stairs.getFacing() == BlockFace.NORTH) return 0;
        else if (stairs.getFacing() == BlockFace.SOUTH) return 180;
        else return 404;
    }
}