package net.rlbot.api.common;

import java.util.regex.Pattern;

public class Text {

    private static final Pattern TAG_REGEXP = Pattern.compile("<[^>]*>");

    public static String removeTags(String str)
    {
        return TAG_REGEXP.matcher(str).replaceAll("");
    }
}
