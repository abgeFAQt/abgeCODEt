package de.abgecodet.devathlon.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

	/*
	 * Author: Creepah__
	 */
	
	private static FileConfiguration cfg;
	private static File file;
	
	
	public static void loadConfig() {
		file = createFile("config");
		cfg = YamlConfiguration.loadConfiguration(file);
		
		createConfigDefaults();
		
	}
	
	
	private static void createConfigDefaults() {
		cfg.options().copyDefaults(true);
		cfg.options().header("# © Copyright by Team abgeCODEt");
		cfg.addDefault("1", "-------------------------------------");
		cfg.addDefault("2", "Setuplink: https://www.gommehd.net/threads/orebattle-2-devathlon-team-abgecodet.294891/");
		cfg.addDefault("3", "-------------------------------------");
		cfg.addDefault("points", 2500);
		cfg.addDefault("4", "Anzahl der Punkte die erreicht werden müssen, damit das gegnerische Team nicht respawnen kann");
		cfg.addDefault("gracetime", 5);
		cfg.addDefault("5", "Länge der Schutzzeit nach dem Tod (in Sekunden)");
		try {
			cfg.save(file);
		} catch (IOException ex) {}
	}

	public static File createFile(String filename) {
		File file = new File("plugins/OreBattle/" + filename + ".yml");
		if(file.exists()) {
			return file;
		} else {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				System.err.println("[OreBattle] Could not create file " + filename + ".");
			} finally {
				return file;
			}
		}
	}
	
	public static FileConfiguration getConfig() {
		if(cfg != null) {
			return cfg;
		}
		return null;
	}
	
	public static File getConfigFile() {
		if(file != null) {
			return file;
		}
		return null;
	}
}
