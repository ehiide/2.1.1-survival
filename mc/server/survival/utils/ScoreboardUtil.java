package mc.server.survival.utils;

import mc.server.survival.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardUtil 
{
	@SuppressWarnings("deprecation")
	public static void showScoreboard(Player player)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		assert manager != null;
		Scoreboard scoreboard = manager.getNewScoreboard();
		
		Objective objective = scoreboard.registerNewObjective(" ", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Objective healthbar = scoreboard.registerNewObjective("health", "health");
        healthbar.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthbar.setRenderType(RenderType.HEARTS);
        healthbar.setDisplayName(ChatColor.RED + "❤");

		final String worldName = player.getWorld().getName();

		if (worldName.equalsIgnoreCase("survival"))
			objective.setDisplayName(ColorUtil.formatHEX("&7--{ &c&lSURVIVAL &7}--"));
		else if (worldName.equalsIgnoreCase("surowce"))
			objective.setDisplayName(ColorUtil.formatHEX("&7--{ &c&lSUROWCE &7}--"));
		else if (worldName.equalsIgnoreCase("survival_nether"))
			objective.setDisplayName(ColorUtil.formatHEX("&7--{ &c&lNETHER &7}--"));
		else if (worldName.equalsIgnoreCase("survival_the_end"))
			objective.setDisplayName(ColorUtil.formatHEX("&7--{ &c&lTHE END &7}--"));
		
		Score score = objective.getScore(ColorUtil.formatHEX(" &7 "));
		score.setScore(15);
		
		score = objective.getScore(ColorUtil.formatHEX("&c&l» &7" + player.getName()));
		score.setScore(14);
		
		score = objective.getScore(ColorUtil.formatHEX(" &8> &a" + DataManager.getInstance().getLocal(player).getRank()));
		score.setScore(13);
		
		score = objective.getScore(ColorUtil.formatHEX("  &7  "));
		score.setScore(12);
		
		score = objective.getScore(ColorUtil.formatHEX("&c&l» &7" + DataManager.getInstance().getLocal(player).getMoney() + " monet(y)"));
		score.setScore(11);

		score = objective.getScore(ColorUtil.formatHEX(" &8> &aSwiat blizej nieokreslony"));

		if (worldName.equalsIgnoreCase("survival"))
			score = objective.getScore(ColorUtil.formatHEX(" &8> &aSwiat survivalowy"));
		else if (worldName.equalsIgnoreCase("surowce"))
			score = objective.getScore(ColorUtil.formatHEX(" &8> &aSwiat surowcowy"));
		else if (worldName.equalsIgnoreCase("survival_nether"))
			score = objective.getScore(ColorUtil.formatHEX(" &8> &aSwiat netheru"));
		else if (worldName.equalsIgnoreCase("survival_the_end"))
			score = objective.getScore(ColorUtil.formatHEX(" &8> &aSwiat endu"));

		score.setScore(10);
		
		player.setScoreboard(scoreboard);
	}
}