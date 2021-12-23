package mc.server.survival.managers;

import mc.server.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public class FileManager 
{
	private FileManager() {}

	static FileManager instance = new FileManager();
		
	public static FileManager getInstance() 
	{
		return instance;
	}
	
	FileConfiguration dataConfiguration, gangsConfiguration, settingsConfiguration, whitelistConfiguration;
	
	public FileConfiguration data() { return dataConfiguration; }
	public FileConfiguration settings() { return settingsConfiguration; }
	public FileConfiguration gangs() { return gangsConfiguration; }
	public FileConfiguration whitelist() { return whitelistConfiguration; }

	File dataFile, gangsFile, settingsFile, whitelistFile;
	
	public void start(Plugin plugin)
	{
		dataFile = new File(plugin.getDataFolder(), "data.yml");
		gangsFile = new File(plugin.getDataFolder(), "gangs.yml");
		settingsFile = new File(plugin.getDataFolder(), "settings.yml");
		whitelistFile = new File(plugin.getDataFolder(), "whitelist.yml");

		dataConfiguration = plugin.getConfig();
		gangsConfiguration = new YamlConfiguration();
		settingsConfiguration = new YamlConfiguration();
		whitelistConfiguration = new YamlConfiguration();

		/*
			Main plugin folder.
		 */

		if (!plugin.getDataFolder().exists()) 
		{
			plugin.getDataFolder().mkdir();
			Logger.log("Pomyslnie utworzono katalog plikow plug-inu!");
		}

		/*
			Files.
		 */

        load(dataConfiguration, dataFile, "data.yml");
        load(gangsConfiguration, gangsFile, "gangs.yml");
        load(settingsConfiguration, settingsFile, "settings.yml");
        load(whitelistConfiguration, whitelistFile, "whitelist.yml");

		/*
			Exists of each file.
		 */

        checkExist(dataConfiguration, dataFile, FileType.DATA, "data.yml");
        checkExist(gangsConfiguration, gangsFile, FileType.GANGS, "gangs.yml");

		checkExist(whitelistConfiguration, whitelistFile, FileType.WHITELIST, "whitelist.yml");

		if (whitelistConfiguration.get("whitelist") == null)
		{
			whitelistConfiguration.set("whitelist", new ArrayList<String>());
			this.save(FileType.WHITELIST);
		}

		if (!settingsFile.exists())
		{
			plugin.saveResource("settings.yml", false);
			Logger.log("Pomyslnie stworzono plik settings.yml!");

			InputStream inputStream = plugin.getResource("settings.yml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			settingsConfiguration = YamlConfiguration.loadConfiguration(reader);
			Logger.log("Pomyslnie zaladowano plik settings.yml!");
		}
	}

	public enum FileType { DATA, GANGS, SETTINGS, WHITELIST }
		
	public void save(FileType type)
	{
		switch (type)
		{
			case DATA -> {
				try { dataConfiguration.save(dataFile); }
							 catch (IOException e) { Logger.log("Nie udalo sie zapisac bazy danych graczy!"); }
			}

			case GANGS -> {
				try { gangsConfiguration.save(gangsFile); }
						 	catch (IOException e) { Logger.log("Nie udalo sie zapisac bazy danych ganu!"); }
			}

			case SETTINGS -> {
				try { settingsConfiguration.save(settingsFile); }
							catch (IOException e) { Logger.log("Nie udalo sie zapisac pliku konfiguracyjnego!"); }
			}

			case WHITELIST -> {
				try { whitelistConfiguration.save(whitelistFile); }
							catch (IOException e) { Logger.log("Nie udalo sie zapisac whitelisty graczy!"); }
			}

			default -> Logger.log("Wystapil blad podczas zapisywania plikow.");
		}
	}

	public void load(FileConfiguration fileConfiguration, File file, String fileName)
    {
        try
        {
            fileConfiguration.load(file);
            Logger.log("Pomyslnie zaladowano plik " + fileName + "!");
        }
        catch (IOException | InvalidConfigurationException e)
        {
            Logger.log("Nie udalo sie odnalezc pliku " + fileName + ", trwa jego synteza...");
        }
    }

    public void checkExist(FileConfiguration fileConfiguration, File file, FileType fileType, String fileName)
    {
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
                Logger.log("Pomyslnie stworzono plik " + fileName + "!");

                try
                {
                    fileConfiguration.load(file);
                    Logger.log("Pomyslnie zaladowano plik " + fileName + "!");
                }
                catch (InvalidConfigurationException e)
                {
                    Logger.log("Nie udalo sie zaladowac pliku " + fileName + ". Prosimy o zresetowanie pliku!");
                }

                this.save(fileType);
            }
            catch (IOException e) { Logger.log("Nie udalo sie stworzyc pliku " + fileName + "!"); }
        }
    }

	public Object getConfigValue(String path) { return settingsConfiguration.get(path); }
}