package net.rlbot.client;

import net.runelite.client.ui.FontManager;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class ClassPreloader
{
    public static void preload()
    {
        // This needs to enumerate the system fonts for some reason, and that takes a while
        FontManager.getRunescapeSmallFont();

        // This needs to load a timezone database that is mildly large
        ZoneId.of("Europe/London");

        // This just needs to call 20 different DateTimeFormatter constructors, which are slow
        Object unused = DateTimeFormatter.BASIC_ISO_DATE;
    }
}