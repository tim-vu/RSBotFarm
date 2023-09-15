package net.rlbot.client.spoofing;

public class SystemSpoofing {

    public static void spoofProperties() {

        System.setProperty("os.arch", "amd64");
        System.setProperty("java.vendor", "Eclipse Adoptium");
        System.setProperty("java.version", "11.0.20");

    }

}
