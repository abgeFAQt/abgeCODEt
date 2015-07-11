package de.abgecodet.devathlon.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import de.abgecodet.devathlon.commands.OreBattle_Command;
import de.abgecodet.devathlon.core.Devathlon;
import de.abgecodet.devathlon.core.GameStatus;
import de.abgecodet.devathlon.core.Global;
import de.abgecodet.devathlon.sql.MySQL;
import de.abgecodet.devathlon.util.Tablist;
import de.abgecodet.devathlon.util.TagManager;

public class playerjoinevent implements Listener {
	
	/*
	 * Author: abgeFAQt
	 */
	
	private static Devathlon plugin;
    public playerjoinevent(Devathlon plugin) {
        this.plugin = plugin;
    }
	
    public static HashMap<String, Integer> rank = new HashMap<String, Integer>();
    
	@EventHandler
	public void on(PlayerJoinEvent e) {
	
	
		final Player p = e.getPlayer();
		
		Tablist tab = new Tablist();
		tab.setFooter("§3OreBattle v1.0");
		tab.setHeader("§a" + Bukkit.getServerName());
		tab.send(p);
		
		if (Devathlon.Status == GameStatus.Lobby) {
			
			/*TagManager tm = new TagManager(p);
			tm.changeTag(tm.getTagByPermissions());
			tm.setTag();*/

			e.setJoinMessage("§7> "+ p.getDisplayName() + " §3hat das Spiel betreten. (" + Bukkit.getOnlinePlayers().size() + "/"+ Bukkit.getMaxPlayers() + ")");
		
			if(Bukkit.getOnlinePlayers().size() == 5 && Devathlon.Timer > 10) {
				Devathlon.Timer = 10;
			}
			
			clearupperson(p);
			createPlayerStats(p);
			
			p.getInventory().setItem(0, createItemstack(Material.COMPASS, "§9Teamauswahl §7<Rechtsklick>", 1, (byte)0));
			p.getInventory().setItem(8, createItemstack(Material.SLIME_BALL, "§9Zurück zur Lobby §7<Rechtsklick>", 1, (byte)0));
		if (Devathlon.sql == true) {
			p.getInventory().setItem(4, createItemstack(Material.NETHER_STAR, "§9Kitauswahl §7<Rechtsklick>", 1, (byte)0));
		}
			
			p.getInventory().setHeldItemSlot(0);
			p.teleport(OreBattle_Command.getLocation("Lobby", true));
			p.sendMessage(Devathlon.prefix + "§3Willkommen bei OreBattle v1.0");
			
		} else {
			
			e.setJoinMessage("");
			
			p.getInventory().clear();
			p.getInventory().setItem(0, createItemstack(Material.REDSTONE_BLOCK, "§cTeam Rot §7<Rechtsklick>", 1, (byte)0));
			p.getInventory().setItem(1, createItemstack(Material.LAPIS_BLOCK, "§9Team Blau §7<Rechtsklick>", 1, (byte)0));
			p.getInventory().setItem(8, createItemstack(Material.SLIME_BALL, "§9Zurück zur Lobby §7<Rechtsklick>", 1, (byte)0));

			TagManager tm = new TagManager(p);
			tm.changeTag("7");
			tm.setTag();
			p.setPlayerListName("§7[§cX§7] §8| §7" + p.getName());
			p.setDisplayName("§7[§cX§7] " + p.getName());
			
			Global.spectator.add(p);
			
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.hidePlayer(p);
				for (Player spec : Global.spectator) {
					spec.showPlayer(p);
					p.showPlayer(spec);
				}
			}
			
			p.setAllowFlight(true);
			p.setFlying(true);
			p.sendMessage(Devathlon.prefix + "§3Du bist jetzt Spectator.");
			p.teleport(OreBattle_Command.getLocation("Spectator", true));
			
		}
	}
	

	public static ItemStack createItemstack(Material m, String name, int amount, Byte damage) {
		ItemStack stack = new ItemStack(m, amount, damage);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);

		return stack;
	}
	
	public void createPlayerStats(Player p) {
		boolean hasEintrag = false;
		ResultSet rs = MySQL.query("SELECT ID FROM playerstats WHERE uuid='" + p.getUniqueId().toString() + "'");

		try {
			rs.last();
			if (rs.getRow() == 0) {
				rs = MySQL.query("SELECT ID FROM playerstats WHERE Player='" + p.getName() + "'");
				rs.last();
				if (rs.getRow() > 0) {
					MySQL.update("UPDATE playerstats SET uuid = '" + p.getUniqueId().toString() + "' WHERE Player ='" + p.getName() + "'");
				}
			}
			rs.beforeFirst();

			if (rs.next()) {
				if (rs != null) {
					hasEintrag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (hasEintrag == false) {
			MySQL.update("INSERT INTO playerstats(uuid,Player,Toetungen,Tode,SpieleGewonnen,SpieleGespielt,Punkte) VALUES ('" + p.getUniqueId().toString() + "','" + p.getName() + "','0','0','0','0','0')");
		}
	}
	
	public static void clearupperson(Player p) {
		p.setAllowFlight(false);
		p.setLevel(0);
		p.setExp(0);
		p.setFoodLevel(20);
		p.setSaturation(10);
		p.setHealth(20D);
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setHeldItemSlot(0);
		ItemStack Air = new ItemStack(Material.AIR);
		pi.setHelmet(Air);
		pi.setChestplate(Air);
		pi.setLeggings(Air);
		pi.setBoots(Air);
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}
}