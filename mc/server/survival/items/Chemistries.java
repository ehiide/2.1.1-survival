package mc.server.survival.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Chemistries
{
    private Chemistries() {}

    static Chemistries instance = new Chemistries();

    public static Chemistries getInstance()
    {
        return instance;
    }

    public static final ChemistryItem opium = new ChemistryItem("Opium", new String[]{" &8> &7Opis: Brak."}, Material.GUNPOWDER, new Affinity(404));

    // from opium
    public static final ChemistryItem alprazolam = new ChemistryItem("Alprazolam", new String[]{" &8> &7Opis: Brak."}, Material.GUNPOWDER, new Affinity(10));
    public static final ChemistryItem metylomorfina = new ChemistryItem("Metylomorfina", new String[]{" &8> &7Opis: Brak."}, Material.GUNPOWDER, new Affinity(20));
    public static final ChemistryItem morfina = new ChemistryItem("Morfina", new String[]{" &8> &7Opis: Brak."}, Material.GUNPOWDER, new Affinity(40));
    public static final ChemistryItem heroina = new ChemistryItem("Heroina", new String[]{" &8> &7Opis: Brak."}, Material.GUNPOWDER, new Affinity(90));
    public static final ChemistryItem fentanyl = new ChemistryItem("Fentanyl", new String[]{" &8> &7Opis: Brak."}, Material.GUNPOWDER, new Affinity(150));

    public static final ChemistryItem amina = new ChemistryItem("Amina", new String[]{""}, Material.SUGAR, new Affinity(404, 0, 0, 0));

    // from amina
    public static final ChemistryItem metyloamina = new ChemistryItem("Metyloamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static final ChemistryItem metylenoamina = new ChemistryItem("Metylenoamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static final ChemistryItem fenyloamina = new ChemistryItem("Fenyloamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static final ChemistryItem fluoroamina = new ChemistryItem("Fluoroamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static final ChemistryItem dimetoamina = new ChemistryItem("Dimetoamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(404, 404, 404, 404));

    // from metyloamina
    public static final ChemistryItem metamfetamina = new ChemistryItem("Metamfetamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(25, 20, 15, -8));
    public static final ChemistryItem metafedron = new ChemistryItem("Metafedron", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(13, 31, 9, -2));
    public static final ChemistryItem metylon = new ChemistryItem("Metylon", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(17, 16, 14, -9));
    public static final ChemistryItem metylometkatynon = new ChemistryItem("Metylometkatynon", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(21, 12, -7, -2));

    // from metylenoamina
    public static final ChemistryItem MDA = new ChemistryItem("MDA", new String[]{" &8> &7Opis: MDA jest najpotezniejszym stymulantem na", "   &7serwerze."}, Material.SUGAR, new Affinity(4, 14, 50, -5));
    public static final ChemistryItem MDMA = new ChemistryItem("MDMA", new String[]{" &8> &7Opis: MDMA jest najsilniejsza substancja euforyczna", "   &7na serwerze."}, Material.SUGAR, new Affinity(55, 35, 20, 0));
    public static final ChemistryItem MDPV = new ChemistryItem("MDPV", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(-2, 9, 42, -20));

    // from fenyloamina
    public static final ChemistryItem amfetamina = new ChemistryItem("Amfetamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(6, 15, 25, -7));
    public static final ChemistryItem mefedron = new ChemistryItem("Mefedron", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(25, 24, 23, 4));
    public static final ChemistryItem klefedron = new ChemistryItem("Klefedron", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(21, 22, 15, 10));

    // from fluoroamina
    public static final ChemistryItem fluoroamfetamina = new ChemistryItem("Fluoroamfetamina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(16, 23, 17, 0));
    public static final ChemistryItem flefedron = new ChemistryItem("Flefedron", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(8, 15, 14, -12));

    // from dimetoamina
    public static final ChemistryItem kokaina = new ChemistryItem("Kokaina", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(12, 50, 25, -5));
    public static final ChemistryItem kleksedron = new ChemistryItem("Kleksedron", new String[]{" &8> &7Opis: Brak."}, Material.SUGAR, new Affinity(10, 17, -11, 4));

    // alcohols
    public static final ChemistryItem piwo = new ChemistryItem("Piwo", new String[]{" &8> &7Opis: jezeli chcesz sie napic dla towarzystwa,", "   &7nie wahaj sie i chwyc za piwo."}, Color.YELLOW, new Affinity(1, 0, 0, 4));
    public static final ChemistryItem wino = new ChemistryItem("Wino", new String[]{" &8> &7Opis: potrzebujesz czegos na romantyczny wieczor", "   &7lub cos slodkiego i owocowego? Oto wino!"}, Color.fromBGR(153, 153, 255), new Affinity(3, -1, 0, 7));
    public static final ChemistryItem szampan = new ChemistryItem("Szampan", new String[]{" &8> &7Opis: gdy w kosciele nowa para, biegnij", "   &7szybko po szampana!"}, Color.RED, new Affinity(0, 1, -1, 11));
    public static final ChemistryItem whisky = new ChemistryItem("Whisky", new String[]{" &8> &7Opis: wytrawny, luksusowy alkohol, ktory", "   &7oswiadczy sie Twoim kubkom smakowym!"}, Color.fromBGR(153, 255, 255), new Affinity(-1, 3, 4, 24));
    public static final ChemistryItem wodka = new ChemistryItem("Wodka", new String[]{" &8> &7Opis: polska czy ruska, zawsze dobrze smakuje", "   &7kto wodki nie pije, ten nie baluje!"}, Color.fromBGR(255, 255, 255), new Affinity(3, 4, 4, 40));
    public static final ChemistryItem bimber = new ChemistryItem("Bimber", new String[]{" &8> &7Opis: legendarny bimber, pedzony przez samego", "   &7proseczka!"}, Color.BLACK, new Affinity(5, 10, -2, 80));

    public ArrayList<ChemistryItem> getChemistries()
    {
        ArrayList<ChemistryItem> chemistries = new ArrayList<>();
        final Field[] fields = this.getClass().getFields();

        for (Field field : fields)
            if (field.getType() == ChemistryItem.class)
            {
                field.setAccessible(true);

                try
                {
                    ChemistryItem chemistryItem = (ChemistryItem) field.get(Chemistries.class);
                    chemistries.add(chemistryItem);
                }
                catch (IllegalAccessException ignored)
                { }
            }

        return chemistries;
    }

    public ChemistryItem byName(final String name)
    {
        for (ChemistryItem chemistryItem : getChemistries())
            if (chemistryItem.getName().equalsIgnoreCase(name))
                return chemistryItem; return null;
    }

    public boolean isKnown(final ItemStack item)
    {
        for (ChemistryItem chemistryItem : getChemistries())
            if (ChemistryDrug.getDrug(chemistryItem).isSimilar(item))
                return true; return false;
    }
}