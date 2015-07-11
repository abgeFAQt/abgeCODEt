package de.abgecodet.devathlon.core;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import de.abgecodet.devathlon.commands.OreBattle_Command;
import de.abgecodet.devathlon.listener.allgemein;
import de.abgecodet.devathlon.listener.kits;
import de.abgecodet.devathlon.listener.playerdeathevent;
import de.abgecodet.devathlon.listener.playerjoinevent;
import de.abgecodet.devathlon.listener.playerquitevent;
import de.abgecodet.devathlon.sql.MySQL;
import de.abgecodet.devathlon.sql.PlayerDataClass;
import de.abgecodet.devathlon.sql.StatsReader.StatsWriter;
import de.abgecodet.devathlon.util.ActionBar;
import de.abgecodet.devathlon.util.FileManager;
import de.abgecodet.devathlon.util.TagManager;
import de.abgecodet.devathlon.util.Title;

public class Devathlon extends JavaPlugin {
	
	/* Author: abgeFAQt / Creepah__ 
	             _                 ____ ___  ____  _____ _   
            __ _| |__   __ _  ___ / ___/ _ \|  _ \| ____| |_ 
           / _` | '_ \ / _` |/ _ \ |  | | | | | | |  _| | __|
          | (_| | |_) | (_| |  __/ |__| |_| | |_| | |___| |_ 
           \__,_|_.__/ \__, |\___|\____\___/|____/|_____|\__|
                       |___/ 
      */                                 
	
	//Strings
	public static File file = new File("plugins/OreBattle", "locs.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static String prefix = "ß9OreBattle ß8| ß7";
	public static String servername = "ß9OreBattleßf";
	public static GameStatus Status;
	public static int Timer = 0;
	
	public static int seconds = 0;
	public static int minutes = 0;
	public static String timecounter = "";
	
	//Devathlon-Instanz
	private static Devathlon devathlon;
	public static boolean sql;
	
	@Override
	public void onEnable() {
		devathlon = this;
		
		
		
		//Vorsichtsmaﬂnahmen
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			public void run() {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamerule doFireTick false");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamerule doMobLoot false");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamerule mobGriefing false");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-off");
			}
		}, 30L);
		
		Status = GameStatus.Lobby;
		Timer = 60;
		
		System.out.print("[]=====================[ OreBattle ]====================[]");
		System.out.print("[]=-              Entwickelt von abgeCODEt            -=[]");
		System.out.print("[]                     ************                     []");
		System.out.print("[]                 Plugin Version : 1.0                 []");
		System.out.print("[]                                                      []");
		System.out.print("[]                Plugin Name : OreBattle               []");
		System.out.print("[]                     ************                     []");
		System.out.print("[]=-       OreBattle wurde erfolgreich aktiviert      -=[]");
		System.out.print("[]======================================================[]");

		getCommand("orebattle").setExecutor(new OreBattle_Command());
		
		MySQL();
		loadLoc();
		registerListeners();
		StartCounter();
		FileManager.loadConfig();
		scoreboard();
	}
	
	public void onDisable() {
		System.out.print("[]=====================[ OreBattle ]====================[]");
		System.out.print("[]=-              Entwickelt von abgeCODEt            -=[]");
		System.out.print("[]                     ************                     []");
		System.out.print("[]                 Plugin Version : 1.0                 []");
		System.out.print("[]                                                      []");
		System.out.print("[]                Plugin Name : OreBattle               []");
		System.out.print("[]                     ************                     []");
		System.out.print("[]=-      OreBattle wurde erfolgreich deaktiviert     -=[]");
		System.out.print("[]======================================================[]");
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new allgemein(), this);
		pm.registerEvents(new playerjoinevent(this), this);
		pm.registerEvents(new playerquitevent(), this);
		pm.registerEvents(new playerdeathevent(this), this);
		pm.registerEvents(new kits(), this);
	}

	public void MySQL() {

		File file = new File("plugins/OreBattle", "MySQL.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		sql = cfg.getBoolean("sql.enable");
		
		cfg.addDefault("sql.enable", false);
		cfg.addDefault("sql.host", "localhost");
		cfg.addDefault("sql.user", "user");
		cfg.addDefault("sql.password", "password");
		cfg.addDefault("sql.port", 3306);
		cfg.addDefault("sql.database", "datenbase");
		cfg.options().copyDefaults(true);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cfg.getBoolean("sql.enable") == true) {

			String host = cfg.getString("sql.host");
			String user = cfg.getString("sql.user");
			String Password = cfg.getString("sql.password");
			String datenbank = cfg.getString("sql.database");
			int port = cfg.getInt("sql.port");
			try {
				Bukkit.getConsoleSender().sendMessage(Devathlon.prefix + "ßbMySQL wird verbunden...");
				MySQL.connect(host, user, Password, datenbank, port);
				MySQL.update("CREATE TABLE IF NOT EXISTS playerstats(id int(11),uuid varchar(36),Player varchar(16),Toetungen int(40),Tode int(40),SpieleGewonnen int(40),SpieleGespielt int(40),Punkte int(40))");
				
			} catch (Exception e) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (all.isOp()) {
						all.sendMessage(Devathlon.prefix + "ßcDie MySQL Daten wurden nicht richtig eingetragen. Bitte trage die MySQL Daten richtig ein oder deaktiviere sie und starte den Server neu.");
						
						Bukkit.getConsoleSender().sendMessage(Devathlon.prefix + "ßcDie MySQL Daten wurden nicht richtig eingetragen. Bitte trage die MySQL Daten richtig ein oder deaktiviere sie und starte den Server neu.");
					}
				}
				Bukkit.getPluginManager().disablePlugin(this);
			}

		}

	}
	
	public static void StartCounter() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Devathlon.getInstance(), new Runnable() {

					@Override
					public void run() {
						Timer -= 1;

						if (Status == GameStatus.Lobby) {
							if ((Timer == 10) | (Timer == 5) | (Timer == 4) | (Timer == 3) | (Timer == 2)) {
								for (Player all : Bukkit.getOnlinePlayers()) {
									all.sendMessage(Devathlon.prefix + "ß3Die Runde beginnt in ße" + Timer + " ß3Sekunden");
									all.playSound(all.getLocation(),Sound.FIREWORK_BLAST2, 1, 1);
								}
							} else if (Timer == 1) {
								for (Player all : Bukkit.getOnlinePlayers()) {
									all.sendMessage(Devathlon.prefix + "ß3Die Runde beginnt in ße" + Timer + " ß3Sekunde");
									all.playSound(all.getLocation(),Sound.FIREWORK_BLAST2, 1, 1);
								}
							} else if (Timer == 0) {
								if (Bukkit.getOnlinePlayers().size() <= 1) {
									for (Player all : Bukkit.getOnlinePlayers()) {
										all.sendMessage(Devathlon.prefix + "ßcWarten auf weitere Spieler!");
										all.playSound(all.getLocation(),Sound.CLICK, 1, 1);
									}

									Timer = 60;
									Status = GameStatus.Lobby;

								} else {

									Timer = 0;
									Status = GameStatus.Ingame;

									actionbarmsg();
									timer();
									points();
									
									// Random Team
									for (Player all : Bukkit.getOnlinePlayers()) {
										if (!Global.team1.contains(all)) {
											if (!Global.team2.contains(all)) {
												if (Global.team2.size() != 4 && Global.team1.size() > Global.team2.size()) {
													Global.team2.add(all);
												} else if (Global.team1.size() != 4) {
													Global.team1.add(all);
												}
											}
										}
									}
									
									for (Player p1 : Global.team1) {
										team1(p1);
										p1.sendMessage(Devathlon.prefix + "ß9Du bist im Team ßcRot ß9(" + Global.team1.size() + ")");
									}
									for (Player p2 : Global.team2) {
										team2(p2);
										p2.sendMessage(Devathlon.prefix + "ß9Du bist im Team ß1Blau ß9(" + Global.team2.size() + ")");
										
									}
									
									for (Player all : Bukkit.getOnlinePlayers()) {
												all.sendMessage("");
												all.sendMessage("                         ß3Kurzerkl‰rung");
												all.sendMessage("");
												all.sendMessage("ß3In OreBattle bekommt dein Team Punkte durch das abbauen von Erzen. Man kann solange respawnen bis das gegnerische Team ße" + Global.points + " ß3Punkte erreicht hat. Also mach dich auf den Weg und r¸ste dich f¸r den Kampf aus.");
										
												all.sendMessage("");
												all.sendMessage("                         ß3Die Teams");
												all.sendMessage("");
										if (Global.team1.size() == 1) {
											all.sendMessage(Devathlon.prefix + "ßcTeam Rotß8: " + "ß6" + Global.team1.get(0).getName());
										} else if (Global.team1.size() == 2) {
											all.sendMessage(Devathlon.prefix + "ßcTeam Rotß8: " + "ß6" + Global.team1.get(0).getName() + ", " + Global.team1.get(1).getName());
										} else if (Global.team1.size() == 3) {
											all.sendMessage(Devathlon.prefix + "ßcTeam Rotß8: " + "ß6" + Global.team1.get(0).getName() + ", " + Global.team1.get(1).getName() + ", " + Global.team1.get(2).getName());
										} else if (Global.team1.size() == 4) {
											all.sendMessage(Devathlon.prefix + "ßcTeam Rotß8: " + "ß6" + Global.team1.get(0).getName() + ", " + Global.team1.get(1).getName() + ", " + Global.team1.get(2).getName() + ", " + Global.team1.get(3).getName());
										}
										
										if (Global.team2.size() == 1) {
											all.sendMessage(Devathlon.prefix + "ß9Team Blauß8: " + "ß6" + Global.team2.get(0).getName());
										} else if (Global.team2.size() == 2) {
											all.sendMessage(Devathlon.prefix + "ß9Team Blauß8: " + "ß6" + Global.team2.get(0).getName() + ", " + Global.team2.get(1).getName());
										} else if (Global.team2.size() == 3) {
											all.sendMessage(Devathlon.prefix + "ß9Team Blauß8: " + "ß6" + Global.team2.get(0).getName() + ", " + Global.team2.get(1).getName() + ", " + Global.team2.get(2).getName());
										} else if (Global.team2.size() == 4) {
											all.sendMessage(Devathlon.prefix + "ß9Team Blauß8: " + "ß6" + Global.team2.get(0).getName() + ", " + Global.team2.get(1).getName() + ", " + Global.team2.get(2).getName() + ", " + Global.team2.get(3).getName());
										}
										
										PlayerDataClass pDCall = Global.re.read(all.getUniqueId().toString());
										pDCall.SpieleGespielt +=1;
										StatsWriter.write(pDCall, all.getUniqueId().toString());
										
										if (Global.kit1.contains(all)) {
											kit1(all);
											return;
										}
										if (Global.kit2.contains(all)) {
											kit2(all);
											return;
										}
										if (Global.kit3.contains(all)) {
											kit3(all);
											return;
										}
										
										all.sendMessage(Devathlon.prefix + "ßcDu hast kein Kit gew‰hlt!");
									}
									
								}
							}
						} else if (Status == GameStatus.Ingame) {
							
							//Nothing
							
						} else if (Status == GameStatus.Restarting) {
							if (Timer == 15) {
								for (Player all : Bukkit.getOnlinePlayers()) {
									all.sendMessage(Devathlon.prefix + "ß9Diese Runde dauerte: ße" + timecounter);
									all.sendMessage(Devathlon.prefix + "ßcDer Server startet in ße" + Timer + " ßcSekunden neu");
								}
							} else if ((Timer == 10) || (Timer == 5)
									| (Timer == 4) | (Timer == 3) | (Timer == 2)) {
								for (Player all : Bukkit.getOnlinePlayers()) {
									all.sendMessage(Devathlon.prefix + "ßcDer Server startet in ße" + Timer + " ßcSekunden neu");
								}
							} else if (Timer == 1) {
								for (Player all : Bukkit.getOnlinePlayers()) {
									all.sendMessage(Devathlon.prefix	+ "ßcDer Server startet in ße" + Timer + " ßcSekunde neu");
								}
								Devathlon.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
							}
						}


					}
				}, 0L, 20L);

	}

	public static void actionbarmsg() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable() {
				
				ActionBar bar = new ActionBar();
				
				@Override
					public void run() {
						for (Player all : Bukkit.getOnlinePlayers()) {

							bar.setMessage("ßcTeam Rot ße" + Global.pointsteam1 + " ß7| ße" + Global.pointsteam2 + " ß9Team Blau");
							bar.send(all);
						}
					}
				}, 0L, 5L);
	}
	
public static void scoreboard() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable() {

			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj = board.registerNewObjective("aaa", "bbb");
			
			Scoreboard board2 = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj2 = board2.registerNewObjective("aaa", "bbb");
			
			@Override
				public void run() {
					for (Player all : Bukkit.getOnlinePlayers()) {

						if (Devathlon.Status == GameStatus.Lobby) {
							
							obj2.setDisplayName(Devathlon.servername);
							obj2.setDisplaySlot(DisplaySlot.SIDEBAR);
														
							Score a = obj2.getScore(Bukkit.getOfflinePlayer("ß3W‰hle"));
							a.setScore(1);
							
							Score b = obj2.getScore(Bukkit.getOfflinePlayer("ß3dein Team"));
							b.setScore(0);
							
							all.setScoreboard(board2);
							
						} else /*if (Main.Status == GameStatus.Ingame)*/ {
						
							obj.setDisplayName("ßfOreBattle ß8| ß6" + timecounter);
							obj.setDisplaySlot(DisplaySlot.SIDEBAR);
														
							Score team1 = obj.getScore(Bukkit.getOfflinePlayer("ßcTeam Rot"));
							team1.setScore(Global.team1.size());
							
							Score team2 = obj.getScore(Bukkit.getOfflinePlayer("ß9Team Blau"));
							team2.setScore(Global.team2.size());
							
							all.setScoreboard(board);
							
						}
						
					}

				}
			}, 0L, 5L);
	}
	
	public static void team1(Player p) {

		ItemStack item1 = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta1 = (LeatherArmorMeta) item1.getItemMeta();
		meta1.setColor(Color.RED);
		item1.setItemMeta(meta1);

		ItemStack item2 = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta2 = (LeatherArmorMeta) item2.getItemMeta();
		meta2.setColor(Color.RED);
		item2.setItemMeta(meta2);

		ItemStack item3 = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta meta3 = (LeatherArmorMeta) item3.getItemMeta();
		meta3.setColor(Color.RED);
		item3.setItemMeta(meta3);

		ItemStack item4 = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta meta4 = (LeatherArmorMeta) item4.getItemMeta();
		meta4.setColor(Color.RED);
		item4.setItemMeta(meta4);

		clearupperson(p);

		p.getInventory().setItem(39, item1);
		p.getInventory().setItem(38, item2);
		p.getInventory().setItem(37, item3);
		p.getInventory().setItem(36, item4);

		TagManager tm = new TagManager(p);
		tm.changeTag("c");
		tm.setTag();
		p.setPlayerListName("ßcRot ß8 | ß7"+ p.getName());;
		p.setDisplayName("ßc"+ p.getName());;
		p.teleport(OreBattle_Command.getLocation("spawn1", true));
		
	}

	public static void team2(Player p) {

		ItemStack item1 = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta1 = (LeatherArmorMeta) item1.getItemMeta();
		meta1.setColor(Color.BLUE);
		item1.setItemMeta(meta1);

		ItemStack item2 = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta2 = (LeatherArmorMeta) item2.getItemMeta();
		meta2.setColor(Color.BLUE);
		item2.setItemMeta(meta2);

		ItemStack item3 = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta meta3 = (LeatherArmorMeta) item3.getItemMeta();
		meta3.setColor(Color.BLUE);
		item3.setItemMeta(meta3);

		ItemStack item4 = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta meta4 = (LeatherArmorMeta) item4.getItemMeta();
		meta4.setColor(Color.BLUE);
		item4.setItemMeta(meta4);

		clearupperson(p);

		p.getInventory().setItem(39, item1);
		p.getInventory().setItem(38, item2);
		p.getInventory().setItem(37, item3);
		p.getInventory().setItem(36, item4);

		TagManager tm = new TagManager(p);
		tm.changeTag("9");
		tm.setTag();
		p.setPlayerListName("ß9Blau ß8 | ß7"+ p.getName());;
		p.setDisplayName("ß9"+ p.getName());;
		p.teleport(OreBattle_Command.getLocation("spawn2", true));
	}
	
	public static void kit1(Player p) {

		ItemStack item1 = new ItemStack(Material.STONE_PICKAXE);
		ItemMeta meta1 = item1.getItemMeta();
		item1.setItemMeta(meta1);

		ItemStack item2 = new ItemStack(Material.FURNACE);
		ItemMeta meta2 = item2.getItemMeta();
		item2.setItemMeta(meta2);

		ItemStack item3 = new ItemStack(Material.COAL, 10);
		ItemMeta meta3 = item3.getItemMeta();
		item3.setItemMeta(meta3);

		clearupperson(p);

		p.getInventory().setItem(0, item1);
		p.getInventory().setItem(1, item2);
		p.getInventory().setItem(2, item3);
		
		p.sendMessage(Devathlon.prefix + "ßaDu hast das Kit ßeBergarbeiter ßaerhalten");
	}
	
	public static void kit2(Player p) {

		ItemStack item1 = new ItemStack(Material.STONE_AXE);
		ItemMeta meta1 = item1.getItemMeta();
		item1.setItemMeta(meta1);

		ItemStack item2 = new ItemStack(Material.WORKBENCH);
		ItemMeta meta2 = item2.getItemMeta();
		item2.setItemMeta(meta2);

		ItemStack item3 = new ItemStack(Material.APPLE, 3);
		ItemMeta meta3 = item3.getItemMeta();
		item3.setItemMeta(meta3);

		clearupperson(p);

		p.getInventory().setItem(0, item1);
		p.getInventory().setItem(1, item2);
		p.getInventory().setItem(2, item3);
		
		p.sendMessage(Devathlon.prefix + "ßaDu hast das Kit ßeHolzf‰ller ßaerhalten");
	}
	
	public static void kit3(Player p) {

		ItemStack item1 = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta1 = item1.getItemMeta();
		meta1.spigot().setUnbreakable(true);
		item1.setItemMeta(meta1);

		ItemStack item2 = new ItemStack(Material.RAW_FISH);
		ItemMeta meta2 = item2.getItemMeta();
		item2.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
		item2.setItemMeta(meta2);

		ItemStack item3 = new ItemStack(Material.BOAT);
		ItemMeta meta3 = item3.getItemMeta();
		item3.setItemMeta(meta3);

		clearupperson(p);

		p.getInventory().setItem(0, item1);
		p.getInventory().setItem(1, item2);
		p.getInventory().setItem(2, item3);
		
		p.sendMessage(Devathlon.prefix + "ßaDu hast das Kit ßeHolzf‰ller ßaerhalten");
	}
	
	public static void clearupperson(Player p) {
		p.setAllowFlight(false);
		p.setLevel(0);
		p.setExp(0);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		p.setGameMode(GameMode.SURVIVAL);
		p.setSaturation(10);
		p.setHealth(20D);
		PlayerInventory pi = p.getInventory();
		pi.setHeldItemSlot(0);
		pi.clear();
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}
	
	public static void points() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable() {
			
					@Override
					public void run() {
						
						if (Global.pointsteam1 >= Global.points && Global.t1 != 1) {
							Global.t1 += 1;
							
							Title title1 = new Title("ß6Team Rot hat", "ß6" + Global.points + " Punkte");
							
							for (Player all : Bukkit.getOnlinePlayers()) {
								title1.send(all);
							}
							
						}
						if (Global.pointsteam2 >= Global.points && Global.t2 != 1) {
							Global.t2 += 1;
							
							Title title2 = new Title("ß6Team Blau hat", "ß6" + Global.points + " Punkte");
							
							for (Player all : Bukkit.getOnlinePlayers()) {
								title2.send(all);
							}
							
						}
						
					}
				}, 0L, 20L);

	}
	
	
	public static void timer() {

		Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable() {

					@Override
					public void run() {

						if (Devathlon.Status == GameStatus.Ingame) {
							if (seconds != 59) {
								seconds++;
							} else {
								minutes++;
								seconds = 0;
							}
						}

						String sec = "";
						String min = "";

						if (seconds < 10) {
							sec = "0" + seconds;
						} else {
							sec = String.valueOf(seconds);
						}

						if (minutes < 10) {
							min = "0" + minutes;
						} else {
							min = String.valueOf(minutes);
						}
						timecounter = min + ":" + sec;
					}
				}, 0L, 20L);
	}
	
	public void loadLoc() {

		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Devathlon getInstance() {
		return devathlon;
	}
	
	public static void setInstance(Devathlon instance) {
		devathlon = instance;
	}
	
}
