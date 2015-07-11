package de.abgecodet.devathlon.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.abgecodet.devathlon.sql.StatsReader;
import de.abgecodet.devathlon.util.FileManager;

public class Global {
	
	/*
	 * Author: abgeFAQt
	 */
	
	// Kits
	public static ArrayList<Player> kit1 = new ArrayList<Player>();
	public static ArrayList<Player> kit2 = new ArrayList<Player>();
	public static ArrayList<Player> kit3 = new ArrayList<Player>();
	
	// Teams
	public static ArrayList<Player> spectator = new ArrayList<Player>();
	public static ArrayList<Player> grace = new ArrayList<Player>();
	public static ArrayList<Player> team1 = new ArrayList<Player>();
	public static ArrayList<Player> team2 = new ArrayList<Player>();
	
	public static HashMap<Location, Block> blocks = new HashMap<Location, Block>();
	
	public static int pointsteam1;
	public static int pointsteam2;
	
	public static int t1;
	public static int t2;
	
	public static int points = FileManager.getConfig().getInt("points");
	public static int gracetime = FileManager.getConfig().getInt("gracetime");
	
	// MySQL / StatsReader
	public static StatsReader re = new StatsReader();
	
}
