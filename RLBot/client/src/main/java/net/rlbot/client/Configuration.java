package net.rlbot.client;

import java.io.*;

public class Configuration {
	public static final File OSRSBOT_DIR = new File(System.getProperty("user.home"), ".osrsbot");

	public static final File CACHE_DIR = new File(OSRSBOT_DIR, "cache");

	public static final File SCRIPT_DIR = new File(OSRSBOT_DIR, "scripts");
}
