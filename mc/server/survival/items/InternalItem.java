package mc.server.survival.items;

import mc.server.survival.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InternalItem
{
    private ItemStack itemStack;

    public InternalItem()
    { }

    public ItemStack toItemStack(Material material)
    {
        return new ItemStack(material, 1);
    }

    public ItemStack getFinalItem()
    {
        return this.itemStack;
    }

    public InternalItem buildItem(Material material, String name, String[] lore)
    {
        ItemStack itemStack = toItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> itemLore = new ArrayList<String>();

        if (name != null)
        {
            assert itemMeta != null;
            itemMeta.setDisplayName(ColorUtil.formatHEX("&r" + name));
        }

        if (lore != null)
        {
            itemLore.add("");
            for (String verse : lore)
                itemLore.add(ColorUtil.formatHEX(verse));
            itemLore.add("");
        }

        assert itemMeta != null;
        itemMeta.setLore(itemLore);

        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;

        return this;
    }

    public InternalItem unbreakable()
    {
        ItemMeta itemMeta = this.itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.setUnbreakable(true);

        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;

        return this;
    }

    public InternalItem enchant()
    {
        ItemMeta itemMeta = this.itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;

        return this;
    }

    public InternalItem hideAttributes()
    {
        ItemMeta itemMeta = this.itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;

        return this;
    }

    public ItemStack get(String ID)
    {
        if (ID.equalsIgnoreCase("pickaxe")) return new InternalItem().buildItem(Material.GOLDEN_PICKAXE, "&eNiezniszczalny Kilof", null).unbreakable().getFinalItem();
        else if (ID.equalsIgnoreCase("shovel")) return new InternalItem().buildItem(Material.GOLDEN_SHOVEL, "&eNiezniszczalna Lopata", null).unbreakable().getFinalItem();
        else if (ID.equalsIgnoreCase("hoe")) return new InternalItem().buildItem(Material.GOLDEN_HOE, "&eNiezniszczalna Motyka", null).unbreakable().getFinalItem();
        else if (ID.equalsIgnoreCase("axe")) return new InternalItem().buildItem(Material.GOLDEN_AXE, "&eNiezniszczalna Siekiera", null).unbreakable().getFinalItem();
        else if (ID.equalsIgnoreCase("crossbow")) return new InternalItem().buildItem(Material.CROSSBOW, "&eNiezniszczalna Kusza", null).unbreakable().getFinalItem();
        else if (ID.equalsIgnoreCase("magic_rod")) return new InternalItem().buildItem(Material.STONE_HOE, "&cKosa Stacha Jonesa", new String[]{"&cLPM &7+ &cLPM &7+ &cLPM", "&eZaklecie przyspieszajace.", "", "&cPPM &7+ &cPPM &7+ &cPPM", "&eZaklecie regenerujace.", "", "&cPPM &7+ &cLPM &7+ &cPPM", "&eZaklecie atakujace.", "", "&cLPM &7+ &cPPM &7+ &cLPM", "&eZaklecie spierdalajaco-znikajace.", ""}).unbreakable().enchant().hideAttributes().getFinalItem();
        return null;
    }
}