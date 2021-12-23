package mc.server.survival.managers;

import mc.server.survival.files.Main;
import mc.server.survival.utils.QuestUtil;
import mc.server.survival.utils.ServerUtil;
import mc.server.survival.utils.TimeUtil;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DataManager
{
    private DataManager() {}

    static DataManager instance = new DataManager();

    FileConfiguration configuration = FileManager.getInstance().data();
    FileConfiguration gangs = FileManager.getInstance().gangs();

    public static DataManager getInstance()
    {
        return instance;
    }

    public LocalData getLocal(final Player player) { return new LocalData(player); }

    public FastData getData() { return new FastData(); }

    private final String[] forceAdm = Main.AUTHORS;

    public class LocalData
    {
        private final Player player;

        public LocalData(Player player)
        {
            this.player = player;
        }

        public String getPlayer() { return player == null ? null : player.getName().toLowerCase(); }

        public void handlePlayer()
        {
            FileManager.getInstance().save(FileManager.FileType.DATA);

            final String playerName = player.getName().toLowerCase();

            if (configuration.get(playerName) != null) return;

            configuration.set(playerName + ".data.name", player.getName());
            configuration.set(playerName + ".data.uuid", ServerUtil.getUUID(player));
            configuration.set(playerName + ".data.rank", "Gracz");

            for (String admin : forceAdm)
                if (playerName.equalsIgnoreCase(admin))
                    configuration.set(playerName + ".data.rank", "Administrator");

            configuration.set(playerName + ".data.gang", "none");
            configuration.set(playerName + ".data.chatcolor", "red");
            configuration.set(playerName + ".data.money", 0);
            configuration.set(playerName + ".data.sp", 0);
            configuration.set(playerName + ".data.kills", 0);
            configuration.set(playerName + ".data.deaths", 0);
            configuration.set(playerName + ".data.logged", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));
            configuration.set(playerName + ".data.mute.date", "2000/01/01 12:00:00");
            configuration.set(playerName + ".data.mute.length", 0);
            configuration.set(playerName + ".data.dungeon.date", "2000/01/01 12:00:00");
            configuration.set(playerName + ".data.dungeon.length", 0);
            configuration.set(playerName + ".data.marry.date", "none");
            configuration.set(playerName + ".data.marry.level", 0);
            configuration.set(playerName + ".data.marry.status", "none");

            configuration.set(playerName + ".data.schowek.items", "none");
            configuration.set(playerName + ".data.schowek.level", 0);

            configuration.set(playerName + ".data.chemistry.serotonine", 0);
            configuration.set(playerName + ".data.chemistry.dopamine", 0);
            configuration.set(playerName + ".data.chemistry.noradrenaline", 0);
            configuration.set(playerName + ".data.chemistry.gaba", 0);

            configuration.set(playerName + ".data.lastlocation.world", WorldUtil.SURVIVAL_SPAWN.getWorld().getName());
            configuration.set(playerName + ".data.lastlocation.x", WorldUtil.SURVIVAL_SPAWN.getX());
            configuration.set(playerName + ".data.lastlocation.y", WorldUtil.SURVIVAL_SPAWN.getY());
            configuration.set(playerName + ".data.lastlocation.z", WorldUtil.SURVIVAL_SPAWN.getZ());
            configuration.set(playerName + ".data.lastlocation.yaw", WorldUtil.SURVIVAL_SPAWN.getYaw());
            configuration.set(playerName + ".data.lastlocation.pitch", WorldUtil.SURVIVAL_SPAWN.getPitch());

            configuration.set(playerName + ".data.home1.world", "none");
            configuration.set(playerName + ".data.home1.x", 0D);
            configuration.set(playerName + ".data.home1.y", 0D);
            configuration.set(playerName + ".data.home1.z", 0D);
            configuration.set(playerName + ".data.home1.yaw", 0F);
            configuration.set(playerName + ".data.home1.pitch", 0F);

            configuration.set(playerName + ".data.home2.world", "none");
            configuration.set(playerName + ".data.home2.x", 0D);
            configuration.set(playerName + ".data.home2.y", 0D);
            configuration.set(playerName + ".data.home2.z", 0D);
            configuration.set(playerName + ".data.home2.yaw", 0F);
            configuration.set(playerName + ".data.home2.pitch", 0F);

            configuration.set(playerName + ".data.quests.1", "#f@0!350");
            configuration.set(playerName + ".data.quests.2", "#f@0!500");
            configuration.set(playerName + ".data.quests.3", "#f@0!400");
            configuration.set(playerName + ".data.quests.4", "#f@0!4");
            configuration.set(playerName + ".data.quests.5", "#f@0!150");
            configuration.set(playerName + ".data.quests.6", "#f@0!10");
            configuration.set(playerName + ".data.quests.7", "#f@1!4");
            configuration.set(playerName + ".data.quests.8", "#f@0!1");
            configuration.set(playerName + ".data.quests.9", "#f@0!10");
            configuration.set(playerName + ".data.quests.10", "#f@0!1");
            configuration.set(playerName + ".data.quests.11", "#f@0!400");
            configuration.set(playerName + ".data.quests.12", "#f@0!1");
            configuration.set(playerName + ".data.quests.13", "#f@0!1");
            configuration.set(playerName + ".data.quests.14", "#f@0!15");

            configuration.set(playerName + ".data.upgrades.vitality", 0);
            configuration.set(playerName + ".data.upgrades.luck", 0);
            configuration.set(playerName + ".data.upgrades.loot", 0);
            configuration.set(playerName + ".data.upgrades.honorable", 0);
            configuration.set(playerName + ".data.upgrades.thiefy", 0);

            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public Object get(String path) { return configuration.get(path); }

        public String getAddress() { return player.spigot().getRawAddress().getHostString(); }

        public String getUUID()
        {
            return (String) configuration.get(getPlayer() + ".data.uuid");
        }

        public String getLogged()
        {
            return (String) configuration.get(getPlayer() + ".data.logged");
        }

        public String getRank()
        {
            return (String) configuration.get(getPlayer() + ".data.rank");
        }

        public void setRank(String rank)
        {
            configuration.set(getPlayer() + ".data.rank", rank);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getGang()
        {
            return ((String) configuration.get(getPlayer() + ".data.gang")).equalsIgnoreCase("none")
                    ? null
                    : (String) configuration.get(getPlayer() + ".data.gang");
        }

        public void setGang(String name)
        {
            configuration.set(getPlayer() + ".data.gang", name.toUpperCase());
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getMoney()
        {
            return (int) configuration.get(getPlayer() + ".data.money");
        }

        public void setMoney(int money)
        {
            configuration.set(getPlayer() + ".data.money", money);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public void addMoney(int money)
        {
            setMoney(getMoney() + money);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public void removeMoney(int money)
        {
            setMoney(getMoney() - money);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getSP()
        {
            return (int) configuration.get(getPlayer() + ".data.sp");
        }

        public void setSP(int sp)
        {
            configuration.set(getPlayer() + ".data.sp", sp);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getKills()
        {
            return (int) configuration.get(getPlayer() + ".data.kills");
        }

        public void setKills(int kills)
        {
            configuration.set(getPlayer() + ".data.kills", kills);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getDeaths()
        {
            return (int) configuration.get(getPlayer() + ".data.deaths");
        }

        public void setDeaths(int deaths)
        {
            configuration.set(getPlayer() + ".data.deaths", deaths);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getMarry()
        {
            return ((String) configuration.get(getPlayer() + ".data.marry.status")).equalsIgnoreCase("none")
                    ? null
                    : (String) configuration.get(getPlayer() + ".data.marry.status");
        }

        public String getMarry(String player)
        {
            return ((String) configuration.get(player + ".data.marry.status")).equalsIgnoreCase("none")
                    ? null
                    : (String) configuration.get(player + ".data.marry.status");
        }

        public void setMarry(String lover)
        {
            configuration.set(getPlayer() + ".data.marry.status", lover);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getMarryDate() { return (String) configuration.get(getPlayer() + ".data.marry.date"); }

        public String getMarryDate(String player) { return (String) configuration.get(player.toLowerCase() + ".data.marry.date"); }

        public void setMarryDate(String date)
        {
            configuration.set(getPlayer() + ".data.marry.date", date);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getMarryLevel() { return (int) configuration.get(getPlayer() + ".data.marry.level"); }

        public int getMarryLevel(String player) { return (int) configuration.get(player + ".data.marry.level"); }

        public void setMarryLevel(int level)
        {
            configuration.set(getPlayer() + ".data.marry.level", level);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public Location getLastLocation()
        {
            String world = (String) configuration.get(getPlayer() + ".data.lastlocation.world");
            double x = (double) configuration.get(getPlayer() + ".data.lastlocation.x");
            double y = (double) configuration.get(getPlayer() + ".data.lastlocation.y");
            double z = (double) configuration.get(getPlayer() + ".data.lastlocation.z");
            float yaw = Float.parseFloat(String.valueOf(configuration.get(getPlayer() + ".data.lastlocation.yaw")));
            float pitch = Float.parseFloat(String.valueOf(configuration.get(getPlayer() + ".data.lastlocation.pitch")));

            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        public void setLastLocation(Location location)
        {
            configuration.set(getPlayer() + ".data.lastlocation.world", location.getWorld().getName());
            configuration.set(getPlayer() + ".data.lastlocation.x", location.getX());
            configuration.set(getPlayer() + ".data.lastlocation.y", location.getY());
            configuration.set(getPlayer() + ".data.lastlocation.z", location.getZ());
            configuration.set(getPlayer() + ".data.lastlocation.yaw", ((float) location.getYaw()));
            configuration.set(getPlayer() + ".data.lastlocation.pitch", ((float) location.getPitch()));
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public void resetLastLocation()
        {
            configuration.set(getPlayer() + ".data.lastlocation.world", WorldUtil.SURVIVAL_SPAWN.getWorld().getName());
            configuration.set(getPlayer() + ".data.lastlocation.x", WorldUtil.SURVIVAL_SPAWN.getX());
            configuration.set(getPlayer() + ".data.lastlocation.y", WorldUtil.SURVIVAL_SPAWN.getY());
            configuration.set(getPlayer() + ".data.lastlocation.z", WorldUtil.SURVIVAL_SPAWN.getZ());
            configuration.set(getPlayer() + ".data.lastlocation.yaw", WorldUtil.SURVIVAL_SPAWN.getYaw());
            configuration.set(getPlayer() + ".data.lastlocation.pitch", WorldUtil.SURVIVAL_SPAWN.getPitch());
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public Location getHome(String which)
        {
            String world = (String) configuration.get(getPlayer() + ".data.home" + which + ".world");
            double x = (double) configuration.get(getPlayer() + ".data.home" + which + ".x");
            double y = (double) configuration.get(getPlayer() + ".data.home" + which + ".y");
            double z = (double) configuration.get(getPlayer() + ".data.home" + which + ".z");
            float yaw = Float.parseFloat(String.valueOf(configuration.get(getPlayer() + ".data.home" + which + ".yaw")));
            float pitch = Float.parseFloat(String.valueOf(configuration.get(getPlayer() + ".data.home" + which + ".pitch")));

            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        public void setHome(String which, Location location)
        {
            configuration.set(getPlayer() + ".data.home" + which + ".world", location.getWorld().getName());
            configuration.set(getPlayer() + ".data.home" + which + ".x", location.getX());
            configuration.set(getPlayer() + ".data.home" + which + ".y", location.getY());
            configuration.set(getPlayer() + ".data.home" + which + ".z", location.getZ());
            configuration.set(getPlayer() + ".data.home" + which + ".yaw", location.getYaw());
            configuration.set(getPlayer() + ".data.home" + which + ".pitch", location.getPitch());
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public void setSchowek(List<ItemStack> contents)
        {
            configuration.set(getPlayer() + ".data.schowek.items", contents);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public List<ItemStack> getSchowek()
        {
            if (!(configuration.get(getPlayer() + ".data.schowek.items") instanceof ArrayList))
                if (((String) configuration.get(getPlayer() + ".data.schowek.items")).equalsIgnoreCase("none")) return null;

            List<ItemStack> whatsInside = (List<ItemStack>) configuration.get(getPlayer() + ".data.schowek.items");

            if (whatsInside == null) return null;
            else return (List<ItemStack>) whatsInside;
        }

        public int getSchowekLevel()
        {
            return (int) configuration.get(getPlayer() + ".data.schowek.level");
        }

        public void setSchowekLevel(int level)
        {
            configuration.set(getPlayer() + ".data.schowek.level", level);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getChatColor()
        {
            return (String) configuration.get(getPlayer() + ".data.chatcolor");
        }

        public String getChatColor(String player)
        {
            return (String) configuration.get(player.toLowerCase() + ".data.chatcolor");
        }

        public void setChatColor(String color)
        {
            configuration.set(getPlayer() + ".data.chatcolor", color);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getDungeoned(String player)
        {
            return (String) configuration.get(player.toLowerCase() + ".data.dungeon.date");
        }

        public void setDungeoned(String date)
        {
            configuration.set(getPlayer() + ".data.dungeon.date", date);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getDungeonLength(String player)
        {
            return (int) configuration.get(player.toLowerCase() + ".data.dungeon.length");
        }

        public void setDungeonLength(int length)
        {
            configuration.set(getPlayer() + ".data.dungeon.length", length);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public boolean isDungeoned()
        {
            return TimeUtil.getDifferenceInSeconds(getDungeoned(getPlayer())) < getDungeonLength(getPlayer());
        }

        public String getMute(String player)
        {
            return (String) configuration.get(player.toLowerCase() + ".data.mute.date");
        }

        public void setMute(String date)
        {
            configuration.set(getPlayer() + ".data.mute.date", date);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getMuteLength(String player)
        {
            return (int) configuration.get(player.toLowerCase() + ".data.mute.length");
        }

        public void setMuteLength(int length)
        {
            configuration.set(getPlayer() + ".data.mute.length", length);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getSerotonine()
        {
            return (int) configuration.get(getPlayer() + ".data.chemistry.serotonine");
        }

        public void setSerotonine(int amount)
        {
            configuration.set(getPlayer() + ".data.chemistry.serotonine", amount);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getDopamine()
        {
            return (int) configuration.get(getPlayer() + ".data.chemistry.dopamine");
        }

        public void setDopamine(int amount)
        {
            configuration.set(getPlayer() + ".data.chemistry.dopamine", amount);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getNoradrenaline()
        {
            return (int) configuration.get(getPlayer() + ".data.chemistry.noradrenaline");
        }

        public void setNoradrenaline(int amount)
        {
            configuration.set(getPlayer() + ".data.chemistry.noradrenaline", amount);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getGABA()
        {
            return (int) configuration.get(getPlayer() + ".data.chemistry.gaba");
        }

        public void setGABA(int amount)
        {
            configuration.set(getPlayer() + ".data.chemistry.gaba", amount);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getQuest(int number) { return (String) configuration.get(getPlayer() + ".data.quests." + number); }

        public void setQuest(int number, String id)
        {
            configuration.set(getPlayer() + ".data.quests." + number, id);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public int getQuestCompleting(int quest)
        {
            int current = QuestUtil.getInstance().getReader(getQuest(quest)).getCurrent();
            int requied = QuestUtil.getInstance().getReader(getQuest(quest)).getRequired();
            double curr = Double.parseDouble(String.valueOf(current));
            double req = Double.parseDouble(String.valueOf(requied));
            double math = (curr / req) * 100;

            return (int) math;
        }

        public void addQuestPoint(int quest)
        {
            configuration.set(getPlayer() + ".data.quests." + quest, QuestUtil.getInstance().getReader(getQuest(quest)).addCurrent());
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getQuestBar(int quest)
        {
            int percentage = getQuestCompleting(quest);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("&a");

            for (int progress = 0; progress < (percentage / 2); progress++)
                stringBuilder.append(":");

            stringBuilder.append("&8");

            for (int left = 0; left <= (53 - (stringBuilder.length() - left) ); left++)
                stringBuilder.append(":");

            return stringBuilder.append(" &7(").append(percentage).append("%)").toString();
        }

        public int getUpgradeLevel(String player, String upgrade)
        {
            return (int) configuration.get(player.toLowerCase() + ".data.upgrades." + upgrade.toLowerCase());
        }

        public void addUpgradeLevel(String player, String upgrade)
        {
            configuration.set(player + ".data.upgrades." + upgrade, (int) configuration.get(player + ".data.upgrades." + upgrade) + 1);
            FileManager.getInstance().save(FileManager.FileType.DATA);
        }

        public String getUpgradeBar(String player, String upgrade, String type)
        {
            StringBuilder stringBuilder = new StringBuilder();
            int tier = getUpgradeLevel(player.toLowerCase(), upgrade);

            if (tier > 0)
            {
                stringBuilder.append(type);

                for (int color = 0; color < tier; color++)
                    for (int fullness = 0; fullness < 10; fullness++)
                        stringBuilder.append(":");
            }

            if (tier < 5)
            {
                stringBuilder.append("&7");

                for (int fullness = 0; fullness < 10; fullness++)
                    stringBuilder.append(":");

                if (tier < 4)
                {
                    stringBuilder.append("&8");

                    for (int black = 0; black < 5 - (tier + 1); black++)
                        for (int fullness = 0; fullness < 10; fullness++)
                            stringBuilder.append(":");
                }
            }

            return stringBuilder.toString();
        }

        public void handleGangs()
        {
            FileManager.getInstance().save(FileManager.FileType.GANGS);

            if (gangs.get("default") != null) return;

            gangs.set("default.name", "DFLT");
            gangs.set("default.members", new ArrayList<String>().add("player"));
            gangs.set("default.lider", "player");
            gangs.set("default.friendlyfire", false);
            gangs.set("default.baza", "none");

            gangs.set("default.cosmetics.color", "white");
            gangs.set("default.cosmetics.prefixes", "normal");
            gangs.set("default.cosmetics.star", false);
            gangs.set("default.cosmetics.chat", false);

            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public void createGang(String name)
        {
            ArrayList<String> members = new ArrayList<>();
            members.add(getPlayer());
            name = name.toUpperCase();

            gangs.set(name + ".name", name.toUpperCase());
            gangs.set(name + ".members", members);
            gangs.set(name + ".lider", getPlayer());
            gangs.set(name + ".friendlyfire", true);
            gangs.set(name + ".baza", "none");
            gangs.set(name + ".cosmetics.color", "white");
            gangs.set(name + ".cosmetics.prefixes", "normal");
            gangs.set(name + ".cosmetics.star", false);
            gangs.set(name + ".cosmetics.chat", false);

            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public boolean isExist(String name)
        {
            return gangs.get(name.toUpperCase()) != null;
        }

        public void removeGang(String name)
        {
            if (gangs.get(name.toUpperCase()) == null) return;

            gangs.set(name.toUpperCase(), null);

            for (String player : getInstance().getData().playerList())
                if (configuration.get(player + ".data.gang").toString().equalsIgnoreCase(name))
                    configuration.set(player + ".data.gang", "none");

            FileManager.getInstance().save(FileManager.FileType.DATA);
            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public int getMembers(String name)
        {
            ArrayList<String> members = (ArrayList<String>) Objects.requireNonNull(gangs.get(name.toUpperCase() + ".members"));

            return members.size();
        }

        public ArrayList<String> getPlayerMembers(String name)
        {
            return (ArrayList<String>) Objects.requireNonNull(gangs.get(name.toUpperCase() + ".members"));
        }

        public void addPlayerMembers(String name, String adder)
        {
            gangs.set(adder + ".data.gang", name.toUpperCase());

            ArrayList<String> members = (ArrayList<String>) Objects.requireNonNull(gangs.get(name.toUpperCase() + ".members"));
            members.add(adder);
            gangs.set(name.toUpperCase() + ".members", members);

            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public String getLider(String name)
        {
            return gangs.get(name.toUpperCase() + ".lider") + "";
        }

        public void setLider(Player player)
        {
            gangs.set(getGang() + ".lider", player.getName());

            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public boolean getFriendlyFire(String name)
        {
            return (boolean) gangs.get(name.toUpperCase() + ".friendlyfire");
        }

        public void toggleFriendlyFire(String name)
        {
            if (getFriendlyFire(name))
                gangs.set(name.toUpperCase() + ".friendlyfire", false);
            else
                gangs.set(name.toUpperCase() + ".friendlyfire", true);

            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public void setBase(String name, Location location)
        {
            gangs.set(name.toUpperCase() + ".baza.world", location.getWorld().getName());
            gangs.set(name.toUpperCase() + ".baza.x", location.getX());
            gangs.set(name.toUpperCase() + ".baza.y", location.getY());
            gangs.set(name.toUpperCase() + ".baza.z", location.getZ());
            gangs.set(name.toUpperCase() + ".baza.yaw", location.getYaw());
            gangs.set(name.toUpperCase() + ".baza.pitch", location.getPitch());
            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public Location getBase(String name)
        {
            if (gangs.get(name.toUpperCase() + ".baza.world") == null) return null;

            if (gangs.get(name.toUpperCase() + ".baza.world") instanceof String)
                if (((String) gangs.get(name.toUpperCase() + ".baza.world")).equalsIgnoreCase("none")) return null;

            String world = (String) gangs.get(name.toUpperCase() + ".baza.world");
            double x = (double) gangs.get(name.toUpperCase() + ".baza.x");
            double y = (double) gangs.get(name.toUpperCase() + ".baza.y");
            double z = (double) gangs.get(name.toUpperCase() + ".baza.z");
            float yaw = Float.parseFloat(String.valueOf(gangs.get(name.toUpperCase() + ".baza.yaw")));
            float pitch = Float.parseFloat(String.valueOf(gangs.get(name.toUpperCase() + ".baza.pitch")));

            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        public void removePlayer(Player player)
        {
            if (getLocal(player).getGang() == null) return;

            ArrayList<String> members = (ArrayList<String>) Objects.requireNonNull(gangs.get(getLocal(player).getGang() + ".members"));
            members.remove(player.getName());

            if (!members.isEmpty())
                gangs.set(getLocal(player).getGang() + ".members", members);

            if (getLider(getLocal(player).getGang()).equalsIgnoreCase(player.getName()))
                removeGang(getLocal(player).getGang());

            configuration.set(getLocal(player).getPlayer() + ".data.gang", "none");

            FileManager.getInstance().save(FileManager.FileType.DATA);
            FileManager.getInstance().save(FileManager.FileType.GANGS);
        }

        public String getColor(String name)
        {
            return (String) gangs.get(name.toUpperCase() + ".cosmetics.color");
        }

        public void setColor(String name, String color)
        {
            gangs.set(name.toUpperCase() + ".cosmetics.color", color);

            FileManager.getInstance().save(FileManager.FileType.GANGS);

            for (String playerName : getPlayerMembers(name))
                if (Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline())
                {
                    Player player = Bukkit.getPlayer(playerName);
                    ServerUtil.reloadContents(player);
                }
        }

        public String getPrefixes(String name)
        {
            return (String) gangs.get(name.toUpperCase() + ".cosmetics.prefixes");
        }

        public void setPrefixes(String name, String prefixes)
        {
            gangs.set(name.toUpperCase() + ".cosmetics.prefixes", prefixes);

            FileManager.getInstance().save(FileManager.FileType.GANGS);

            for (String playerName : getPlayerMembers(name))
                if (Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline())
                {
                    Player player = Bukkit.getPlayer(playerName);
                    ServerUtil.reloadContents(player);
                }
        }

        public boolean getStar(String name)
        {
            return (boolean) gangs.get(name.toUpperCase() + ".cosmetics.star");
        }

        public void setStar(String name, boolean star)
        {
            gangs.set(name.toUpperCase() + ".cosmetics.star", star);

            FileManager.getInstance().save(FileManager.FileType.GANGS);

            for (String playerName : getPlayerMembers(name))
                if (Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline())
                {
                    Player player = Bukkit.getPlayer(playerName);
                    ServerUtil.reloadContents(player);
                }
        }

        public boolean getChat(String name)
        {
            return (boolean) gangs.get(name.toUpperCase() + ".cosmetics.chat");
        }

        public void setChat(String name, boolean chat)
        {
            gangs.set(name.toUpperCase() + ".cosmetics.chat", chat);

            FileManager.getInstance().save(FileManager.FileType.GANGS);

            for (String playerName : getPlayerMembers(name))
                if (Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline())
                {
                    Player player = Bukkit.getPlayer(playerName);
                    ServerUtil.reloadContents(player);
                }
        }
    }

    public class FastData
    {
        public FastData() { }

        public List<String> playerList()
        {
            Set<String> keys = configuration.getKeys(false);
            return keys.stream().toList();
        }
    }
}