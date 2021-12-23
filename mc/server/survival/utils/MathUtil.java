package mc.server.survival.utils;

import java.util.Random;

public class MathUtil
{
    public static boolean chanceOf(final int chance)
    {
        final Random r = new Random();
        final int number = r.nextInt(100);

        return number <= chance;
    }

    public static boolean chanceOf(final float chance)
    {
        if (chance == 0) return false;

        final Random r = new Random();
        final int number = r.nextInt(100);

        return number <= chance;
    }

    public static boolean chanceOf(final double chance)
    {
        if (chance == 0) return false;

        final Random r = new Random();
        final int number = r.nextInt(100);

        return number <= chance;
    }

    public static boolean isInteger(final String arg)
    {
        try { Integer.parseInt(arg); }
        catch (NumberFormatException e) { return false; } return true;
    }
}