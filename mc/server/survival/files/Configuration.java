package mc.server.survival.files;

import mc.server.survival.utils.ColorUtil;

public class Configuration 
{
	/*
		Prefixes.
	 */

	public static final String SERVER_PREFIX = ColorUtil.formatHEX("#f83044[Survival]");
	public static final String SERVER_FULL_PREFIX = ColorUtil.formatHEX("#f83044[Survival] #8c8c8c");
	public static final String SERVER_LOGGING_PREFIX = ColorUtil.formatHEX("#f83044[Autoryzacja] #8c8c8c");
	public static final String CONSOLE_PREFIX = ColorUtil.formatHEX("&7[Survival]");
	public static final String CONSOLE_FULL_PREFIX = ColorUtil.formatHEX("&7[Survival] ");

	/*
		Server configuration.
	 */
	
	public static final int SERVER_MAX_PLAYERS = 40;
	public static final String SERVER_ICON_NAME = "server-icon.png";
	public static final String SERVER_MOTD = "&f&l✦ &7Wlacz &c&lMINECRAFT 1.18 &7i dolacz do nas! &f&l✦ \n&d&lINSTAGRAM &5@realehide &7| &b&lDISCORD &9c66DVjhJba";

	/*
		Plug-in configuration.
	 */

	public static boolean SERVER_BLOCK_THE_END = false;
	public static boolean SERVER_BLOCK_NETHER = false;
	public static boolean SERVER_TERRAIN_PROTECTION = true;
	public static final int SERVER_SPAWN_PROTECTION = 256;

	public static final int SERVER_TELEPORT_REQUEST_TIME = 60;
	public static final int SERVER_MARRY_REQUEST_TIME = 30;
	public static final int SERVER_GANG_REQUEST_TIME = 30;
}