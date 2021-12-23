package mc.server.survival.events;

import mc.server.survival.items.Chemistries;
import mc.server.survival.items.ChemistryDrug;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DataManager;
import mc.server.survival.utils.InventoryUtil;
import mc.server.survival.utils.MathUtil;
import mc.server.survival.utils.QuestUtil;
import mc.server.survival.utils.ServerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.Random;

public class PlayerFish implements Listener
{
    @EventHandler
    public void onEvent(PlayerFishEvent event)
    {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH)
        {
            Player player = event.getPlayer();

            if (MathUtil.chanceOf(1 + (DataManager.getInstance().getLocal(player).getSerotonine() / 10) + DataManager.getInstance().getLocal(player).getUpgradeLevel(player.getName(), "luck")))
                legendaryLootFound(player);
            else if (MathUtil.chanceOf(5 + (DataManager.getInstance().getLocal(player).getSerotonine() / 10)))
                mysteryLootFound(player);
            else if (MathUtil.chanceOf(10 + (DataManager.getInstance().getLocal(player).getSerotonine() / 10)))
                lootFound(player);
        }
    }

    private void lootFound(Player player)
    {
        ArrayList<String> loot = new ArrayList<>();

        int money = 30 + new Random().nextInt(70);
        boolean piwo = MathUtil.chanceOf(50);

        loot.add("#fcff33" + money + " monet(y)&7");
        if (piwo)
            loot.add("&ePiwo &f(x1)&7");

        String loots = loot.toString().substring(1, loot.toString().length() - 1);

        DataManager.getInstance().getLocal(player).addMoney(money);
        ServerUtil.reloadContents(player);

        if (piwo)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.piwo));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.piwo));

        ChatManager.sendMessage(player, "&e★ #ffc936Wyglada na to, ze znalazles morski skarb, a oto jego zawartosc: " + loots);
    }

    private void mysteryLootFound(Player player)
    {
        ArrayList<String> loot = new ArrayList<>();

        int money = 100 + new Random().nextInt(150);
        boolean piwo = MathUtil.chanceOf(70);
        boolean wino = MathUtil.chanceOf(50);
        boolean szampan = MathUtil.chanceOf(30);

        loot.add("#fcff33" + money + " monet(y)&7");
        if (piwo)
            loot.add("&ePiwo &f(x1)&7");
        if (wino)
            loot.add("&eWino &f(x1)&7");
        if (szampan)
            loot.add("&eSzampan &f(x1)&7");

        String loots = loot.toString().substring(1, loot.toString().length() - 1);

        DataManager.getInstance().getLocal(player).addMoney(money);
        ServerUtil.reloadContents(player);

        if (piwo)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.piwo));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.piwo));

        if (wino)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.wino));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.wino));

        if (szampan)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.szampan));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.szampan));

        ChatManager.sendMessage(player, "&e★ #ffc936Wyglada na to, ze znalazles mityczny morski skarb, a oto jego zawartosc: " + loots);
    }

    private void legendaryLootFound(Player player)
    {
        ArrayList<String> loot = new ArrayList<>();

        int money = 200 + new Random().nextInt(150);
        boolean piwo = MathUtil.chanceOf(90);
        boolean wino = MathUtil.chanceOf(70);
        boolean szampan = MathUtil.chanceOf(50);
        boolean whisky = MathUtil.chanceOf(30);
        boolean xanax = MathUtil.chanceOf(10);

        loot.add("#fcff33" + money + " monet(y)&7");
        if (piwo)
            loot.add("&ePiwo &f(x1)&7");
        if (wino)
            loot.add("&eWino &f(x1)&7");
        if (szampan)
            loot.add("&eSzampan &f(x1)&7");
        if (whisky)
            loot.add("&eWhisky &f(x1)&7");
        if (xanax)
            loot.add("&aALprazolam &f(x1)&7");

        String loots = loot.toString().substring(1, loot.toString().length() - 1);

        DataManager.getInstance().getLocal(player).addMoney(money);
        ServerUtil.reloadContents(player);

        if (piwo)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.piwo));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.piwo));

        if (wino)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.wino));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.wino));

        if (szampan)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.szampan));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.szampan));

        if (whisky)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.whisky));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.whisky));

        if (xanax)
            if (InventoryUtil.isFullInventory(player))
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), ChemistryDrug.getDrug(Chemistries.alprazolam));
            else
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.alprazolam));

        ChatManager.sendMessage(player, "&e★ #ffc936Wyglada na to, ze znalazles legendarny morski skarb, a oto jego zawartosc: " + loots);
        QuestUtil.manageQuest(player, 14);
    }
}