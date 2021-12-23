package mc.server.survival.utils;

import mc.server.Logger;
import mc.server.survival.files.Main;
import mc.server.survival.items.Chemistries;
import mc.server.survival.items.ChemistryDrug;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RecipeUtil 
{
	private RecipeUtil() {}

	static RecipeUtil instance = new RecipeUtil();

	public static RecipeUtil getInstance()
	{
		return instance;
	}

	private static int loadedInternalRecipes = 0;
	private static int loadedChemistryRecipes = 0;

	public void addRecipes()
	{
		createInternalRecipe(structureRecipe(toItemStack(Material.SADDLE), "saddle",
				Map.of('L', Material.LEATHER,
						'S', Material.STRING,
						'I', Material.IRON_INGOT,
						'0', Material.AIR),
				"LLL", "S0S", "I0I"));
		createInternalRecipe(structureRecipe(toItemStack(Material.NAME_TAG), "name_tag",
				Map.of('P', Material.PAPER,
						'S', Material.STRING,
						'I', Material.IRON_INGOT),
				"SIS", "PPP", "SIS"));
		createInternalRecipe(structureRecipe(toItemStack(Material.DIAMOND_HORSE_ARMOR), "diamond_horse_armor",
				Map.of('D', Material.DIAMOND,
						'0', Material.AIR),
				"DDD", "DDD", "D0D"));
		createInternalRecipe(structureRecipe(toItemStack(Material.EXPERIENCE_BOTTLE), "experience_bottle",
				Map.of('G', Material.GLOWSTONE_DUST,
						'I', Material.IRON_NUGGET,
						'B', Material.GLASS_BOTTLE,
						'0', Material.AIR),
				"0G0", "IBI", "0G0"));

		Logger.asyncLog("&7Zaladowano receptury (Survival): " + loadedInternalRecipes + "/4");

		createChemistryRecipe(structureRecipe(ChemistryDrug.getDrug(Chemistries.piwo), "piwo", Map.of('W', Material.WHEAT, 'B', Material.GLASS_BOTTLE), "WWW", "WBW", "WWW"));
		createChemistryRecipe(structureRecipe(ChemistryDrug.getDrug(Chemistries.wino), "wino", Map.of('A', Material.AIR, 'S', Material.SWEET_BERRIES, 'V', Material.SUGAR, 'B', Material.GLASS_BOTTLE), "ASA", "VBV", "ASA"));
		createChemistryRecipe(structureRecipe(ChemistryDrug.getDrug(Chemistries.bimber), "bimber", Map.of('A', Material.APPLE, 'G', Material.GOLDEN_APPLE, 'B', Material.GLASS_BOTTLE), "AGA", "GBG", "AGA"));

		Logger.asyncLog("&7Zaladowano receptury (Chemistry3): " + loadedChemistryRecipes + "/3");
	}

	public static ItemStack toItemStack(final Material material)
	{
		return new ItemStack(material, 1);
	}

	public static void createInternalRecipe(ShapedRecipe recipe)
	{
		Bukkit.addRecipe(recipe); loadedInternalRecipes++;
	}
	
	public static void createChemistryRecipe(ShapedRecipe recipe)
	{
		Bukkit.addRecipe(recipe); loadedChemistryRecipes++;
	}

	public static ShapedRecipe structureRecipe(final ItemStack itemToRecipe, final String ID, @NotNull Map ingredients, String... shape)
	{
		final NamespacedKey key = new NamespacedKey(Main.getInstance(), ID);
		ShapedRecipe recipe = new ShapedRecipe(key, itemToRecipe);

		try
		{
			recipe.shape(shape);

			for (String keySet : shape)
				for (char keyMap : keySet.toCharArray())
					recipe.setIngredient(keyMap, (Material) ingredients.get(keyMap));
		}
		catch (Exception e) { Logger.log("&cWystapil problem podczas tworzenia receptury."); }

		return recipe;
	}
}