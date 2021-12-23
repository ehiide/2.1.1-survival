package mc.server.survival.utils;

import mc.server.survival.managers.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagUtil
{
    public static void showNametag(final Player player)
    {
        Scoreboard scoreboard = player.getScoreboard();
        final String name = player.getName().toLowerCase();
        final String teamName = "nametag";

        if (scoreboard.getTeam(teamName) != null)
            scoreboard.getTeam(teamName).unregister();

        Team team = scoreboard.registerNewTeam(teamName);

        team.addEntry(name);
        team.setPrefix(ColorUtil.formatHEX(
                ChatUtil.getGangInChat(DataManager.getInstance().getLocal(player).getGang()) +
                ChatUtil.returnMarryPrefix(player)));
        team.setColor(ChatUtil.returnDefinedPlayerColor(name));
        team.setCanSeeFriendlyInvisibles(false);
        team.setAllowFriendlyFire(true);

        player.setScoreboard(scoreboard);
    }
}