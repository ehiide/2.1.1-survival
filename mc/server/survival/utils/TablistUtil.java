package mc.server.survival.utils;

import mc.server.survival.managers.DataManager;
import org.bukkit.entity.Player;

public class TablistUtil
{
	public static void showTablist(Player player)
	{
		final String worldName = player.getWorld().getName();

		if (worldName.equalsIgnoreCase("survival"))
			player.setPlayerListHeaderFooter(
					ColorUtil.formatHEX("\n&7--{ &c&lS U R V I V A L &7}-- \n\n "
							+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
							"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
		else if (worldName.equalsIgnoreCase("surowce"))
			player.setPlayerListHeaderFooter(
			ColorUtil.formatHEX("\n&7--{ &c&lS U R O W C E &7}-- \n\n "
					+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
					"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
		else if (worldName.equalsIgnoreCase("survival_nether"))
			player.setPlayerListHeaderFooter(
					ColorUtil.formatHEX("\n&7--{ &c&lN E T H E R &7}-- \n\n "
							+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
							"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
		else
			player.setPlayerListHeaderFooter(
					ColorUtil.formatHEX("\n&7--{ &c&lT H E   E N D &7}-- \n\n "
							+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
							"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
	}

	public static void showPlayerTag(Player player)
	{
		player.setPlayerListName(ColorUtil.formatHEX(ChatUtil.getGangInChat(DataManager.getInstance().getLocal(player).getGang()) + ChatUtil.returnMarryPrefix(player) + ChatUtil.returnPlayerColor(player) + player.getName()));
	}
}