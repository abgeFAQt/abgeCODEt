package de.abgecodet.devathlon.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import de.abgecodet.devathlon.commands.OreBattle_Command;
import de.abgecodet.devathlon.core.Devathlon;
import de.abgecodet.devathlon.core.GameStatus;
import de.abgecodet.devathlon.core.Global;
import de.abgecodet.devathlon.sql.PlayerDataClass;
import de.abgecodet.devathlon.sql.StatsReader.StatsWriter;
import de.abgecodet.devathlon.util.TagManager;

public class playerdeathevent implements Listener {
	
	/*
	 * Author: abgeFAQt
	 */
	
	private static Devathlon plugin;
    public playerdeathevent(Devathlon plugin) {
        this.plugin = plugin;
    }
	
	@EventHandler
	public void on(PlayerDeathEvent e) {
		
		final Player toter = e.getEntity();
		
		e.setDeathMessage("");
		e.setDroppedExp(0);
		e.setKeepInventory(false);
		
		for (ItemStack stack : e.getDrops()) {
			
			if (stack.getType() == Material.LEATHER_HELMET 
					|| stack.getType() == Material.LEATHER_CHESTPLATE
					|| stack.getType() == Material.LEATHER_LEGGINGS
					|| stack.getType() == Material.LEATHER_BOOTS
					|| stack.getType() == Material.NETHER_STAR) {
				e.getDrops().remove(stack.getType());
			}
			
		}
		
		PlayerDataClass pDCtoter = Global.re.read(toter.getUniqueId().toString());
		pDCtoter.Tode +=1;
		StatsWriter.write(pDCtoter, toter.getUniqueId().toString());
		
		if (e.getEntity().getKiller() instanceof Player){
			
			Player killer = (Player) e.getEntity().getKiller();
			double dhealth =  ((Damageable)killer).getHealth();
			String shealth = String.valueOf(dhealth/2);
			shealth = shealth.substring(0,shealth.indexOf(".")+2);
			
			for (Player all : Bukkit.getOnlinePlayers())
			all.sendMessage(Devathlon.prefix + toter.getDisplayName() + " §7wurde von " + killer.getDisplayName() + " §7[§e" + shealth + "❤§7] §7getötet");
			
			PlayerDataClass pDCkiller = Global.re.read(killer.getUniqueId().toString());
			pDCkiller.Toetungen +=1;
			pDCkiller.Punkte +=25;
			killer.sendMessage(Devathlon.prefix + "§e+ 25 Punkte");
			StatsWriter.write(pDCkiller, killer.getUniqueId().toString());
			
		} else {
			
			for (Player all : Bukkit.getOnlinePlayers())
			all.sendMessage(Devathlon.prefix + toter.getDisplayName() + " §7ist gestorben");
			
		}
		
		if (Global.team1.contains(toter)) {
			Global.pointsteam1 -= 5;
		} else if (Global.team2.contains(toter)) {
			Global.pointsteam2 -= 5;
		}
		
		if (Global.team1.contains(toter)) {
			if (Global.pointsteam2 >= Global.points) {
				spec(toter);
				for (Player all : Bukkit.getOnlinePlayers())
				all.sendMessage(Devathlon.prefix + "§cTeam Rot §7besitzt noch §e" + Global.team1.size() + " §7Spieler");
			
			if (Global.team1.size() == 0) {
				
				for (Player all : Bukkit.getOnlinePlayers())
				all.sendMessage(Devathlon.prefix + "§9Team Blau §ahat gewonnen");
				
				for (Player t2 : Global.team2) {
					PlayerDataClass pDCt2 = Global.re.read(t2.getUniqueId().toString());
					pDCt2.SpieleGewonnen +=1;
					pDCt2.Punkte +=125;
					StatsWriter.write(pDCt2, t2.getUniqueId().toString());
					t2.sendMessage(Devathlon.prefix + "§e+ 125 Punkte");					
				}
				
				Devathlon.Timer = 16;
				Devathlon.Status = GameStatus.Restarting;
			}
			return;
			}
		}
		
		if (Global.team2.contains(toter)) {
			if (Global.pointsteam1 >= Global.points) {
				spec(toter);
				for (Player all : Bukkit.getOnlinePlayers())
				all.sendMessage(Devathlon.prefix + "§9Team Blau §7besitzt noch §e" + Global.team2.size() + " §7Spieler");
			
			if (Global.team2.size() == 0) {
				for (Player all : Bukkit.getOnlinePlayers())
				all.sendMessage(Devathlon.prefix + "§cTeam Rot §ahat gewonnen");
				
				for (Player t1 : Global.team1) {
					PlayerDataClass pDCt1 = Global.re.read(t1.getUniqueId().toString());
					pDCt1.SpieleGewonnen +=1;
					pDCt1.Punkte +=125;
					StatsWriter.write(pDCt1, t1.getUniqueId().toString());
					t1.sendMessage(Devathlon.prefix + "§e+ 125 Punkte");
				}
				
				Devathlon.Timer = 16;
				Devathlon.Status = GameStatus.Restarting;
			}
			return;
			}
		}
	
		toter.sendMessage(Devathlon.prefix + "§3Du konntest respawnen, da das gegnerische Team noch keine " + Global.points + " Punkte erreicht hat");
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            
            @Override
            public void run() {
            	toter.spigot().respawn();
            	if (Global.team1.contains(toter)) {
            		toter.teleport(OreBattle_Command.getLocation("spawn1", true));
            		Devathlon.team1(toter);
    			} else if (Global.team2.contains(toter)) {
    				toter.teleport(OreBattle_Command.getLocation("spawn2", true));
    				Devathlon.team2(toter);
    			}
            }

        }, 30L);
		
		// Grace
		Global.grace.add(toter);
		
		if (Global.gracetime != 0) {
			toter.sendMessage(Devathlon.prefix + "§cDeine Schutzzeit endet in §7[§e" + Global.gracetime + "§7] §cSekunden");

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					Global.grace.remove(toter);
					toter.sendMessage(Devathlon.prefix + "§cDeine Schutzzeit ist vorbei!");
				}

			}, 20L * Global.gracetime);
		}
	
	}
	
	public static void spec(final Player p) {
		
		Global.spectator.add(p);

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.hidePlayer(p);
			for (Player spec : Global.spectator) {
				spec.showPlayer(p);
				p.showPlayer(spec);
			}
		}
	
		if (Global.team1.contains(p)) {
			Global.team1.remove(p);
			p.sendMessage(Devathlon.prefix + "§3Du konntest nicht respawnen, da das gegnerische Team bereits " + Global.points + " Punkte erreicht hat");
		}
		if (Global.team2.contains(p)) {
			Global.team2.remove(p);
			p.sendMessage(Devathlon.prefix + "§3Du konntest nicht respawnen, da das gegnerische Team bereits " + Global.points +" Punkte erreicht hat");
		}
		if (Global.kit1.contains(p)) {
			Global.kit1.remove(p);
		}
		if (Global.kit2.contains(p)) {
			Global.kit2.remove(p);
		}
		if (Global.kit3.contains(p)) {
			Global.kit3.remove(p);
		}
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            
            @Override
            public void run() {
            	p.spigot().respawn();
        		p.teleport(OreBattle_Command.getLocation("Spectator", true));
        		p.getInventory().clear();
        		p.getInventory().setItem(0, playerjoinevent.createItemstack(Material.REDSTONE_BLOCK, "§cTeam Rot §7<Rechtsklick>", 1, (byte) 0));
        		p.getInventory().setItem(1, playerjoinevent.createItemstack(Material.LAPIS_BLOCK, "§9Team Blau §7<Rechtsklick>", 1, (byte) 0));
        		p.getInventory().setItem(8, playerjoinevent.createItemstack(Material.SLIME_BALL, "§9Zurück zur Lobby §7<Rechtsklick>", 1, (byte) 0));

        		p.setAllowFlight(true);
        		p.setFlying(true);
        		TagManager tm = new TagManager(p);
        		tm.changeTag("7");
        		tm.setTag();
        		p.setPlayerListName("§7[§cX§7] §8| §7" + p.getName());
        		p.setDisplayName("§7" + p.getName());
            }

        }, 20L);
		
	}
	
}	