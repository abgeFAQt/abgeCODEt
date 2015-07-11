package de.abgecodet.devathlon.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.abgecodet.devathlon.core.Devathlon;
import de.abgecodet.devathlon.core.GameStatus;
import de.abgecodet.devathlon.core.Global;
import de.abgecodet.devathlon.util.Title;

public class allgemein implements Listener {
	
	/*
	 * Author: abgeFAQt
	 */
	
	private Inventory inv;

	@EventHandler
	public void on(PlayerAchievementAwardedEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void on(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void on(WeatherChangeEvent  e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void on(PlayerPickupItemEvent e) {
		
		Player p = e.getPlayer();
		if (Global.spectator.contains(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerDropItemEvent e) {
		
		Player p = e.getPlayer();
		if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
			e.setCancelled(true);
			return;
		}
		if (Global.spectator.contains(p)) {
			e.setCancelled(true);
		} else {
			e.setCancelled(false);
		}
	}
	
	@EventHandler
	public void on(EntityDamageEvent e) {

		if (Global.grace.contains(e.getEntity())) {
			e.setCancelled(true);
		}
		if (Global.spectator.contains(e.getEntity())) {
			if (e.getCause().equals(DamageCause.LAVA) | e.getCause().equals(DamageCause.FIRE) | 
					e.getCause().equals(DamageCause.FIRE_TICK) | e.getCause().equals(DamageCause.FALL)) {
				e.setCancelled(true);
			}
		}
		if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e) {

		Player p = (Player) e.getDamager();
		Player p2 = (Player) e.getEntity();
		
		if (Global.spectator.contains(p)) {
			e.setCancelled(true);
		}
		if (Global.grace.contains(p)) {
			e.setCancelled(true);
		}
		if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
			e.setCancelled(true);
		}
		if (Global.team1.contains(p) && Global.team1.contains(p2)) {
			e.setCancelled(true);
		}
		if (Global.team2.contains(p) && Global.team2.contains(p2)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void on(BlockPlaceEvent e) {
		
		Player p = e.getPlayer();
		
		/*int x = OreBattle_Command.getLocation("spawn1", true).getBlockX();
		int y = OreBattle_Command.getLocation("spawn1", true).getBlockY();          
		int z = OreBattle_Command.getLocation("spawn1", true).getBlockZ();
		
		int b = e.getBlock().getLocation().getBlockX();
		
		if (e.getBlock().getLocation() == OreBattle_Command.getLocation("spawn1", true)
				| e.getBlock().getLocation() == OreBattle_Command.getLocation("spawn2", true)
				| b == x &&  b == y+1 && b == z) {
			e.setCancelled(true);
			p.sendMessage(Devathlon.prefix + "§cDu darst hier nichts platzieren");
			return;
		}*/
		
		if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
			e.setCancelled(true);
			return;
		}
		if (Global.spectator.contains(p)) {
			e.setCancelled(true);
			return;
		}
		if (e.getBlock().getType() == Material.GLASS
				| e.getBlock().getType() == Material.OBSIDIAN
				| e.getBlock().getType() == Material.DIAMOND_ORE
				| e.getBlock().getType() == Material.IRON_ORE
				| e.getBlock().getType() == Material.GOLD_ORE
				| e.getBlock().getType() == Material.LAPIS_ORE
				| e.getBlock().getType() == Material.REDSTONE_ORE
				| e.getBlock().getType() == Material.COAL_ORE
				| e.getBlock().getType() == Material.EMERALD_ORE) {
			e.setCancelled(true);
			p.sendMessage(Devathlon.prefix + "§cDu darfst §e" + e.getBlock().getType() + " §cnicht platzieren");
			return;
		}
		
		e.setCancelled(false);
	}
	
	@EventHandler
	public void on(AsyncPlayerChatEvent e) {
		
		Player p = e.getPlayer();
		String msg = e.getMessage();

		if (msg.startsWith("%")) {
			msg = msg.replace("%", "Prozent");
		} else if (msg.contains(" %")) {
			msg = msg.replace("%", "Prozent");
		} else if (msg.contains("%")) {
			msg = msg.replace("%", " Prozent");
		}
		if (msg.contains("<3")) {
			msg = msg.replace("<3", "§4❤§f");
		}
		
		e.setFormat(p.getDisplayName() + "§7: §f" + msg);
		
		if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
			return;
		}
		
		if (Global.team1.contains(p)) {
			e.setCancelled(true);
			if (msg.startsWith("@all")) {
				msg = msg.replace("@all", "");
				for (Player all : Bukkit.getOnlinePlayers()) {
						all.sendMessage("§7[§a@all§7] " + p.getDisplayName() + "§7: §f" + msg);
				}
				return;
			}
				for (Player t1 : Global.team1) {
					t1.sendMessage("§7[§cTEAM§7] " + p.getDisplayName() + "§7: §f" + msg);
				}
			}
			
			if (Global.team2.contains(p)) {
				e.setCancelled(true);
				if (msg.startsWith("@all")) {
					msg = msg.replace("@all", "");
					for (Player all : Bukkit.getOnlinePlayers()) {
							all.sendMessage("§7[§a@all§7] " + p.getDisplayName() + "§7: §f" + msg);
					}
					return;
				}
				for (Player t2 : Global.team2) {
					t2.sendMessage("§7[§1TEAM§7] " + p.getDisplayName() + "§7: §f" + msg);
				}
			}
		
		if (Devathlon.Status != GameStatus.Restarting && Global.spectator.contains(p)) {
			e.setCancelled(true);

			for (Player spec : Global.spectator) {
				spec.sendMessage("§7[§4✖§7] " + p.getDisplayName() + "§7: §f" + msg);
			}
			return;
		}
			
		
	}
	
	@EventHandler
	public void on(PlayerInteractEvent e) {
		
		Player p = e.getPlayer();
			
		if (e.getAction() == Action.PHYSICAL) {
			e.setCancelled(true);
		}
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			//Teamauswahl
			if (e.getMaterial().equals(Material.COMPASS)) {

				p.playSound(p.getLocation(), Sound.CLICK, 3, 1);
				this.inv = p.getServer().createInventory(null, 9, "§9Teamauswahl");
				
				ItemStack item1 = new ItemStack(Material.WOOL, 1, (byte) 14);
				ItemMeta meta1 = item1.getItemMeta();
				meta1.setDisplayName("§cTeam Rot");
				ArrayList<String> lore1 = new ArrayList<>();
				if (Global.team1.size() == 1) {
					lore1.add("§e+ §c" + Global.team1.get(0).getName());
				}
				if (Global.team1.size() == 2) {
					lore1.add("§e+ §c" + Global.team1.get(0).getName());
					lore1.add("§e+ §c" + Global.team1.get(1).getName());
				}
				if (Global.team1.size() == 3) {
					lore1.add("§e+ §c" + Global.team1.get(0).getName());
					lore1.add("§e+ §c" + Global.team1.get(1).getName());
					lore1.add("§e+ §c" + Global.team1.get(2).getName());
				}
				if (Global.team1.size() == 4) {
					lore1.add("§e+ §c" + Global.team1.get(0).getName());
					lore1.add("§e+ §c" + Global.team1.get(1).getName());
					lore1.add("§e+ §c" + Global.team1.get(2).getName());
					lore1.add("§e+ §c" + Global.team1.get(3).getName());
				}
				meta1.setLore(lore1);
				item1.setItemMeta(meta1);

				ItemStack item2 = new ItemStack(Material.WOOL, 1, (byte) 11);
				ItemMeta meta2 = item2.getItemMeta();
				meta2.setDisplayName("§9Team Blau");
				ArrayList<String> lore2 = new ArrayList<>();
				if (Global.team2.size() == 1) {
					lore2.add("§e+ §9" + Global.team2.get(0).getName());
				}
				if (Global.team2.size() == 2) {
					lore2.add("§e+ §9" + Global.team2.get(0).getName());
					lore2.add("§e+ §9" + Global.team2.get(1).getName());
				}
				if (Global.team2.size() == 3) {
					lore2.add("§e+ §9" + Global.team2.get(0).getName());
					lore2.add("§e+ §9" + Global.team2.get(1).getName());
					lore2.add("§e+ §9" + Global.team2.get(2).getName());
				}
				if (Global.team2.size() == 4) {
					lore2.add("§e+ §9" + Global.team2.get(0).getName());
					lore2.add("§e+ §9" + Global.team2.get(1).getName());
					lore2.add("§e+ §9" + Global.team2.get(2).getName());
					lore2.add("§e+ §9" + Global.team2.get(3).getName());
				}
				meta2.setLore(lore2);
				item2.setItemMeta(meta2);
				
				
				ItemStack item3 = new ItemStack(Material.WOOL, 1, (byte) 8);
				ItemMeta meta3 = item3.getItemMeta();
				meta3.setDisplayName("§7Zufälliges Team");
				
				item3.setItemMeta(meta3);

				this.inv.setItem(1, item1);
				this.inv.setItem(4, item3);
				this.inv.setItem(7, item2);
				p.openInventory(this.inv);

			}
			
			if (e.getMaterial().equals(Material.REDSTONE_BLOCK)) {
				if (Global.spectator.contains(p)) {
					Inventory inv = Bukkit.createInventory(null, 27, "§cTeam Rot");
					for (Player players : Bukkit.getOnlinePlayers()) {
						if ((Global.team1.contains(players)) && (players != e.getPlayer())) {
							ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName(players.getDisplayName());
							ArrayList<String> lore = new ArrayList<>();
							
							double dhealth =  ((Damageable)players).getHealth();
							String shealth = String.valueOf(dhealth/2);
							shealth = shealth.substring(0,shealth.indexOf(".")+2);
							
							lore.add("§e" + shealth + "§4❤");
							lore.add("§7<Klick> zum Teleportieren");
							meta.setLore(lore);
							item.setItemMeta(meta);

							inv.addItem(new ItemStack[] { item });
						}
					}
					e.getPlayer().openInventory(inv);
				}
			}
			
			if (e.getMaterial().equals(Material.LAPIS_BLOCK)) {
				if (Global.spectator.contains(p)) {
					Inventory inv = Bukkit.createInventory(null, 27, "§9Team Blau");
					for (Player players : Bukkit.getOnlinePlayers()) {
						if ((Global.team2.contains(players)) && (players != e.getPlayer())) {
							ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName(players.getDisplayName());
							ArrayList<String> lore = new ArrayList<>();
							
							double dhealth =  ((Damageable)players).getHealth();
							String shealth = String.valueOf(dhealth/2);
							shealth = shealth.substring(0,shealth.indexOf(".")+2);
							
							lore.add("§e" + shealth + "§4❤");
							lore.add("§7<Klick> zum Teleportieren");
							meta.setLore(lore);
							item.setItemMeta(meta);

							inv.addItem(new ItemStack[] { item });
						}
					}
					e.getPlayer().openInventory(inv);
				}
			}
		
		}
	}
	
	@EventHandler
	public void on(InventoryClickEvent e) {

		Player p = (Player) e.getWhoClicked();

		try {
		
		if (Devathlon.Status == GameStatus.Lobby) {
			e.setCancelled(true);
		}
		
		if (Global.spectator.contains(p)) {
			e.setCancelled(true);
		}
		
		if (!(Global.spectator.contains(e.getWhoClicked().getName()))) {
			if ((e.getInventory().getName().equalsIgnoreCase("§9Teamauswahl"))) {

				if (e.getCurrentItem().getType() == Material.WOOL
						&& e.getSlot() == 1) {
					if (!(Global.team1.size() >= 4)) {
						if (Global.team1.contains(p)) {
							Global.team1.remove(p);
						}
						if (Global.team2.contains(p)) {
							Global.team2.remove(p);
						}
					Global.team1.add(p);
					p.sendMessage(Devathlon.prefix + "§7Du bist in §cTeam Rot");
					} else {
						p.sendMessage(Devathlon.prefix + "§cTeam Rot §cist bereits voll");
					}
					p.closeInventory();
				}
				if (e.getCurrentItem().getType() == Material.WOOL
						&& e.getSlot() == 4) {
					if (Global.team1.contains(p)) {
						Global.team1.remove(p);
					}
					if (Global.team2.contains(p)) {
						Global.team2.remove(p);
					}
					p.sendMessage(Devathlon.prefix + "§9Du wirst einem §6zufälligen Team §9zugewiesen");
					p.closeInventory();
				}
				if (e.getCurrentItem().getType() == Material.WOOL
						&& e.getSlot() == 7) {
					if (!(Global.team2.size() >= 4)) {
						if (Global.team1.contains(p)) {
							Global.team1.remove(p);
						}
						if (Global.team2.contains(p)) {
							Global.team2.remove(p);
						}
						Global.team2.add(p);
						p.sendMessage(Devathlon.prefix + "§7Du bist in §9Team Blau");
					} else {
						p.sendMessage(Devathlon.prefix + "§1Team Blau §cist bereits voll");
					}		
					p.closeInventory();
				}	
			}
		}
		
		if ((e.getInventory().getName().equalsIgnoreCase("§cTeam Rot") | (e.getInventory().getName().equalsIgnoreCase("§9Team Blau"))
				&& (e.getSlot() == e.getRawSlot()) && (e.getCurrentItem() != null) && (e.getCurrentItem().hasItemMeta()))) {
			String playername = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
			Player player_ = Bukkit.getPlayer(playername);
			if (player_ != null) {
				p.teleport(player_);
				p.sendMessage(Devathlon.prefix + "§7Teleportiere zu "+ player_.getDisplayName());
			}
		}
		} catch (Exception ex) {

		}
	}
	
	@EventHandler
	public void on(final BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if (Global.spectator.contains(p)) {
			e.setCancelled(true);
			return;
		}
		
		if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
			e.setCancelled(true);
			return;
		}
		
		//Team
		if (Global.team1.contains(p)) {
			if (e.getBlock().getType() == Material.DIAMOND_ORE) { 
				Global.pointsteam1 += 15;
				p.sendMessage(Devathlon.prefix + "§e+ 15 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
							@Override
							public void run() {
								Block b = Global.blocks.get(e.getBlock().getLocation());
								
								int x = b.getLocation().getBlockX();
								int y = b.getLocation().getBlockY();          
								int z = b.getLocation().getBlockZ();		
								
								b.getWorld().getBlockAt(x, y, z).setType(Material.DIAMOND_ORE);
								Global.blocks.remove(b.getLocation());
							}
						}, 20L * 120);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.IRON_ORE) {
				Global.pointsteam1 += 10;
				p.sendMessage(Devathlon.prefix + "§e+ 10 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.IRON_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 60);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.EMERALD_ORE) {
				Global.pointsteam1 += 10;
				p.sendMessage(Devathlon.prefix + "§e+ 10 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.EMERALD_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 60);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.GOLD_ORE) {
				Global.pointsteam1 += 3;
				p.sendMessage(Devathlon.prefix + "§e+ 3 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.GOLD_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 30);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.REDSTONE_ORE | e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE) {
				Global.pointsteam1 += 2;
				p.sendMessage(Devathlon.prefix + "§e+ 2 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.REDSTONE_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 30);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.COAL_ORE) {
				Global.pointsteam1 += 1;
				p.sendMessage(Devathlon.prefix + "§e+ 1 Point");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.COAL_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 60);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.LAPIS_ORE) {
				Global.pointsteam1 += 1;
				p.sendMessage(Devathlon.prefix + "§e+ 1 Point");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.LAPIS_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 30);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.OBSIDIAN) {
				Global.pointsteam1 += 500;
				Global.pointsteam2 -= 500;
				
				Title obsidian = new Title("§6Der Obsidian", "§6wurde zerstört");
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					obsidian.send(all);
				}
				for (Player t1 : Global.team1) {
					t1.sendMessage(Devathlon.prefix + "§5+ 500 Points (Obsidian)");
				}
				for (Player t1 : Global.team2) {
					t1.sendMessage(Devathlon.prefix + "§5- 500 Points (Obsidian)");
				}
				
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.playSound(all.getEyeLocation(), Sound.ENDERDRAGON_DEATH, 2, 2);
				}
				
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.OBSIDIAN);
						Global.blocks.remove(b.getLocation());
						
						Title obsidian2 = new Title("§6Der Obsidian", "§6ist respawnt");
						
						for (Player all : Bukkit.getOnlinePlayers()) {
							obsidian2.send(all);
							all.playSound(all.getEyeLocation(), Sound.ENDERDRAGON_GROWL, 2, 2);
						}
					}
				}, 20L * 600);
				e.setCancelled(false);
				return;
			}
			
		} else if (Global.team2.contains(p)) {
			if (e.getBlock().getType() == Material.DIAMOND_ORE) {
				Global.pointsteam2 += 15;
				p.sendMessage(Devathlon.prefix + "§e+ 15 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.DIAMOND_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 120);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.IRON_ORE) {
				Global.pointsteam2 += 10;
				p.sendMessage(Devathlon.prefix + "§e+ 10 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.IRON_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 60);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.EMERALD_ORE) {
				Global.pointsteam2 += 10;
				p.sendMessage(Devathlon.prefix + "§e+ 10 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.EMERALD_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 60);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.GOLD_ORE) {
				Global.pointsteam2 += 3;
				p.sendMessage(Devathlon.prefix + "§e+ 3 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.GOLD_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 30);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.REDSTONE_ORE | e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE) {
				Global.pointsteam2 += 2;
				p.sendMessage(Devathlon.prefix + "§e+ 2 Points");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.REDSTONE_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 30);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.COAL_ORE) {
				Global.pointsteam2 += 1;
				p.sendMessage(Devathlon.prefix + "§e+ 1 Point");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.COAL_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 60);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.LAPIS_ORE) {
				Global.pointsteam2 += 1;
				p.sendMessage(Devathlon.prefix + "§e+ 1 Point");
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.LAPIS_ORE);
						Global.blocks.remove(b.getLocation());
					}
				}, 20L * 30);
				e.setCancelled(false);
				return;
			}
			if (e.getBlock().getType() == Material.OBSIDIAN) {
				Global.pointsteam2 += 500;
				Global.pointsteam1 -= 500;
				
				Title obsidian = new Title("§6Der Obsidian", "§6wurde zerstört");
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					obsidian.send(all);
				}
				for (Player t2 : Global.team2) {
					t2.sendMessage(Devathlon.prefix + "§5+ 500 Points (Obsidian)");
				}
				for (Player t1 : Global.team1) {
					t1.sendMessage(Devathlon.prefix + "§5- 500 Points (Obsidian)");
				}
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.playSound(all.getEyeLocation(), Sound.ENDERDRAGON_DEATH, 2, 2);
				}
				
				Global.blocks.put(e.getBlock().getLocation(), e.getBlock());
				Bukkit.getScheduler().runTaskLater(Devathlon.getInstance(),new Runnable() {
					
					@Override
					public void run() {
						Block b = Global.blocks.get(e.getBlock().getLocation());
						
						int x = b.getLocation().getBlockX();
						int y = b.getLocation().getBlockY();          
						int z = b.getLocation().getBlockZ();		
						
						b.getWorld().getBlockAt(x, y, z).setType(Material.OBSIDIAN);
						Global.blocks.remove(b.getLocation());
						Title obsidian2 = new Title("§6Der Obsidian", "§6ist respawnt");
						
						for (Player all : Bukkit.getOnlinePlayers()) {
							obsidian2.send(all);
							all.playSound(all.getEyeLocation(), Sound.ENDERDRAGON_GROWL, 2, 2);
						}
					
					}
				}, 20L * 600);
				e.setCancelled(false);
				return;
			}
			
		}
		
		if (e.getBlock().getType() == Material.DIAMOND_BLOCK | e.getBlock().getType() == Material.GLASS) {
			e.setCancelled(true);
			p.sendMessage(Devathlon.prefix + "§cDu darfst §e" + e.getBlock().getType() + " §cnicht abbauen");
			return;
		} 
		
		e.setCancelled(false);
	}
}
