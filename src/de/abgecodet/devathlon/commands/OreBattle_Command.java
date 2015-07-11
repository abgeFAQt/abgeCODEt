package de.abgecodet.devathlon.commands;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.abgecodet.devathlon.core.Devathlon;
import de.abgecodet.devathlon.core.GameStatus;
import de.abgecodet.devathlon.core.Global;
import de.abgecodet.devathlon.sql.MySQL;
import de.abgecodet.devathlon.sql.StatsReader.StatsData;

/*
 * Author: abgeFAQt
 */

public class OreBattle_Command implements CommandExecutor {	
	
	public static HashMap<String, Integer> rank = new HashMap<String, Integer>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("Du musst ein Spieler sein, um diesen Befehl ausführen zu können!");
			return true;
		}

		Player p = (Player) sender;
		
			if (args.length == 0) {
				if (p.hasPermission("orebattle.admin")) {
				p.sendMessage("§8-*= §6OreBattle - Commands §8=*-");
				p.sendMessage("§9/orebattle setlobby§8: §eLobbyspawn setzen");
				p.sendMessage("§9/orebattle setspectator§8: §eSpectatorspawn setzen");
				p.sendMessage("§9/orebattle setspawnteam1§8: §eSpawn Team 1 setzen");
				p.sendMessage("§9/orebattle setspawnteam2§8: §eSpawn Team 2 setzen");
				p.sendMessage("§9/orebattle sethologram§8: §eSpawn Hologram setzen");
				p.sendMessage("§9/orebattle stats <player>§8: §eOreBattle Statistik");
				p.sendMessage("§9/orebattle start§8: §eStarte das Spiel");
				p.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-");
				} else {
					p.sendMessage(Devathlon.prefix + "§cKeine Rechte!");
				}
					
			} else {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("setlobby") && p.hasPermission("orebattle.admin")) {
						setLocation("Lobby", p.getLocation(), true);
						p.sendMessage(Devathlon.prefix + "§9Du hast den " + "§6Lobbyspawn" + " §9gesetzt.");
					
					} else if(args[0].equalsIgnoreCase("setspectator") && p.hasPermission("orebattle.admin")){
						setLocation("Spectator", p.getLocation(), true);
						p.sendMessage(Devathlon.prefix + "§9Du hast den " + "§6Spectatorspawn" + " §9gesetzt.");
					
					} else if (args[0].equalsIgnoreCase("setspawnteam1") && p.hasPermission("orebattle.admin")) {
						setLocation("spawn1", p.getLocation(), true);
						p.sendMessage(Devathlon.prefix + "§9Du hast den " + "§6Spawn für Team 1" + " §9gesetzt.");
						
					} else if (args[0].equalsIgnoreCase("setspawnteam2") && p.hasPermission("orebattle.admin")) {
						setLocation("spawn2", p.getLocation(), true);
						p.sendMessage(Devathlon.prefix + "§9Du hast den " + "§6Spawn für Team 2" + " §9gesetzt.");
					
					} else if (args[0].equalsIgnoreCase("stats")) {
						
						if(Devathlon.sql == true) {
						ResultSet rs = MySQL.query("SELECT Player FROM playerstats ORDER BY Punkte DESC");
						int I = 0;

						try {
							while (rs.next()) {
								I++;
								rank.put(rs.getString("Player"), I);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
							p.sendMessage("§8-*= §6OreBattle - Stats von " + p.getDisplayName() + " §8=*-");
							p.sendMessage("§9Position im Ranking§8: §6" + rank.get(p.getName()));
							p.sendMessage("§9Punkte§8: §6"+Global.re.readStats(StatsData.Punkte, p.getName(),p.getUniqueId().toString()));
							p.sendMessage("§9Tötungen§8: §6"+Global.re.readStats(StatsData.Toetungen, p.getName(),p.getUniqueId().toString()));
							p.sendMessage("§9Tode§8: §6"+Global.re.readStats(StatsData.Tode, p.getName(),p.getUniqueId().toString()));
							double kd1 = ((double)Global.re.readStats(StatsData.Toetungen, p.getName(),p.getUniqueId().toString())/Global.re.readStats(StatsData.Tode, p.getName(),p.getUniqueId().toString()));
							if(Global.re.readStats(StatsData.Tode, p.getName(),p.getUniqueId().toString()) == 0)
								Global.re.readStats(StatsData.Toetungen, p.getName(),p.getUniqueId().toString());
							String kd = String.valueOf(kd1); 
							kd = kd.substring(0,kd.indexOf(".")+2);
							
							if(Global.re.readStats(StatsData.Tode, p.getName(),p.getUniqueId().toString()) == 0 && Global.re.readStats(StatsData.Toetungen, p.getName(),p.getUniqueId().toString()) == 0){
								kd = "0";
							}
							p.sendMessage("§9K/D§8: §6"+kd);
							
							p.sendMessage("§9Gespielte Spiele§8: §6"+Global.re.readStats(StatsData.SpieleGespielt, p.getName(),p.getUniqueId().toString()));
							p.sendMessage("§9Gewonnene Spiele§8: §6"+Global.re.readStats(StatsData.SpieleGewonnen, p.getName(),p.getUniqueId().toString()));
							double sieg1 = ((double)100/Global.re.readStats(StatsData.SpieleGespielt, p.getName(),p.getUniqueId().toString())*Global.re.readStats(StatsData.SpieleGewonnen, p.getName(),p.getUniqueId().toString()));
							if(Global.re.readStats(StatsData.Tode, p.getName(),p.getUniqueId().toString()) == 0){
								sieg1 = 100;
							}
							String sieg = String.valueOf(sieg1); 
							sieg = sieg.substring(0,sieg.indexOf(".")+2);
							p.sendMessage("§9Sieg-Wahrscheinlichkeit§8: §6"+sieg+" %");
							p.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-");
						
						
						return true;
						} else {
							p.sendMessage(Devathlon.prefix + "§cDie Statistiken sind zurzeit deaktiviert!");
						}
					} else if (args[0].equalsIgnoreCase("start")) {
						if (Devathlon.Status == GameStatus.Lobby && Devathlon.Timer > 10) { 
						p.sendMessage(Devathlon.prefix + "§9Du hast den " + "§6Counter" + " §9verkürtzt.");
						Devathlon.Timer = 10;
						} else {
							p.sendMessage(Devathlon.prefix + "§cDies ist gerade nicht möglich!");
						}
					
					} else {
						p.sendMessage(Devathlon.prefix + "§cUnbekanntes Argument.");
					}

				} else if (args.length == 2) {
					
					if (Devathlon.sql == true) {
					if (isPlayer(args[1])) {
						Player p2 = Bukkit.getPlayer(args[1]);
					if (args[0].equalsIgnoreCase("stats")) {
						
						ResultSet rs = MySQL.query("SELECT Player FROM playerstats ORDER BY Punkte DESC");
						int I = 0;

						try {
							while (rs.next()) {
								I++;
								rank.put(rs.getString("Player"), I);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
							p.sendMessage("§8-*= §6OreBattle - Stats von " + p2.getName() + " §8=*-");
							p.sendMessage("§9Position im Ranking§8: §6" + rank.get(p2.getName()));
							p.sendMessage("§9Punkte§8: §6"+Global.re.readStats(StatsData.Punkte, p2.getName(),p2.getUniqueId().toString()));
							p.sendMessage("§9Tötungen§8: §6"+Global.re.readStats(StatsData.Toetungen, p2.getName(),p2.getUniqueId().toString()));
							p.sendMessage("§9Tode§8: §6"+Global.re.readStats(StatsData.Tode, p2.getName(),p2.getUniqueId().toString()));
							double kd1 = ((double)Global.re.readStats(StatsData.Toetungen, p2.getName(),p2.getUniqueId().toString())/Global.re.readStats(StatsData.Tode, p2.getName(),p2.getUniqueId().toString()));
							if(Global.re.readStats(StatsData.Tode, p2.getName(),p2.getUniqueId().toString()) == 0)
								Global.re.readStats(StatsData.Toetungen, p2.getName(),p2.getUniqueId().toString());
							String kd = String.valueOf(kd1); 
							kd = kd.substring(0,kd.indexOf(".")+2);
							
							if(Global.re.readStats(StatsData.Tode, p2.getName(),p2.getUniqueId().toString()) == 0 && Global.re.readStats(StatsData.Toetungen, p2.getName(),p2.getUniqueId().toString()) == 0){
								kd = "0";
							}
							p.sendMessage("§9K/D§8: §6"+kd);
							
							p.sendMessage("§9Gespielte Spiele§8: §6"+Global.re.readStats(StatsData.SpieleGespielt, p2.getName(),p2.getUniqueId().toString()));
							p.sendMessage("§9Gewonnene Spiele§8: §6"+Global.re.readStats(StatsData.SpieleGewonnen, p2.getName(),p2.getUniqueId().toString()));
							double sieg1 = ((double)100/Global.re.readStats(StatsData.SpieleGespielt, p2.getName(),p2.getUniqueId().toString())*Global.re.readStats(StatsData.SpieleGewonnen, p2.getName(),p2.getUniqueId().toString()));
							if(Global.re.readStats(StatsData.Tode, p2.getName(),p2.getUniqueId().toString()) == 0){
								sieg1 = 100;
							}
							String sieg = String.valueOf(sieg1); 
							sieg = sieg.substring(0,sieg.indexOf(".")+2);
							p.sendMessage("§9Sieg-Wahrscheinlichkeit§8: §6"+sieg+" %");
							p.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-");
							}
						} else {
							p.sendMessage(Devathlon.prefix + "§cSpieler nicht gefunden!");
						}
					} else {
						p.sendMessage(Devathlon.prefix + "§cDie Statistiken sind zurzeit deaktiviert!");
					}
				}
			}
		

		return true;
	}
	public void setLocation(String path, Location loc, boolean direction){
		
		File file = new File("plugins/OreBattle", "locs.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set(path+".world", loc.getWorld().getName());
		cfg.set(path+".x", loc.getX());
		cfg.set(path+".y", loc.getY());
		cfg.set(path+".z", loc.getZ());
		if(direction == true){
			cfg.set(path+".yaw", loc.getYaw());
			cfg.set(path+".pitch", loc.getPitch());

		}
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static Location getLocation(String path, boolean direction){
		Location loc = null;
		
		File file = new File("plugins/OreBattle", "locs.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.contains(path)){
		
		String w = cfg.getString(path+".world");
		double x = cfg.getDouble(path+".x");
		double y = cfg.getDouble(path+".y");
		double z = cfg.getDouble(path+".z");
		double yaw = 0;
		double pitch = 0;
		if(cfg.contains(path+".yaw")){
		yaw = cfg.getDouble(path+".yaw");
		pitch = cfg.getDouble(path+".pitch");
		}

		loc = new Location(Bukkit.getWorld(w), x, y, z);
		if(direction == true){
			loc.setYaw((float) yaw);
			loc.setPitch((float) pitch);
			}
		} else {
			return null;
		}
		return loc;
	}
	
	public boolean isPlayer(String s) {

		if (Bukkit.getPlayer(s) != null) {

			return true;
		} else {
			return false;
		}
	}

}