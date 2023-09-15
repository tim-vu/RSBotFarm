package net.rlbot.internal.util;

import java.util.regex.Pattern;

public class Text {

    private static final Pattern TAG_REGEXP = Pattern.compile("<[^>]*>");

    public static String sanitize(String value)
    {
        if (value == null)
        {
            return null;
        }

        String tagsRemoved = removeTags(value);
        if (tagsRemoved == null)
        {
            return null;
        }

        return sanitizeName(tagsRemoved);
    }

    private static String removeTags(String str)
    {
        return TAG_REGEXP.matcher(str).replaceAll("");
    }

    private static String sanitizeName(String name)
    {
        String cleaned = name.contains("<img") ? name.substring(name.lastIndexOf('>') + 1) : name;
        return cleaned.replace('\u00A0', ' ');
    }
}
