package de.abgecodet.devathlon.listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.abgecodet.devathlon.core.Devathlon;
import de.abgecodet.devathlon.core.Global;
import de.abgecodet.devathlon.sql.StatsReader.StatsData;

public class kits implements Listener {
	
	/*
	 * Author: abgeFAQt
	 */
	
	private Inventory inv;

	@EventHandler
	public void on(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			//Teamauswahl
			if (e.getMaterial().equals(Material.NETHER_STAR)) {
				
				if (Devathlon.sql = true) {
				p.playSound(p.getLocation(), Sound.CLICK, 3, 1);
				this.inv = p.getServer().createInventory(null, 9, "§9Kits");
				
				ItemStack item1 = new ItemStack(Material.STONE_PICKAXE);
				ItemMeta meta1 = item1.getItemMeta();
				meta1.setDisplayName("§eBergarbeiter");
				ArrayList<String> lore1 = new ArrayList<>();
				lore1.add("§bAusrüstung:");
				lore1.add("§e+ §7Steinspitzhacke");
				lore1.add("§e+ §7Ofen");
				lore1.add("§e+ §710 Kohle");
				lore1.add("");
				lore1.add("§7Nutzbar ab:");
				lore1.add("§a1000 Punkte");
				meta1.setLore(lore1);
				item1.setItemMeta(meta1);
				
				ItemStack item2 = new ItemStack(Material.STONE_AXE);
				ItemMeta meta2 = item2.getItemMeta();
				meta2.setDisplayName("§eHolzfäller");
				ArrayList<String> lore2 = new ArrayList<>();
				lore2.add("§bAusrüstung:");
				lore2.add("§e+ §7Steinaxt");
				lore2.add("§e+ §7Werkbank");
				lore2.add("§e+ §73 Äpfel");
				lore2.add("");
				lore2.add("§7Nutzbar ab:");
				lore2.add("§a2500 Punkte");
				meta2.setLore(lore2);
				item2.setItemMeta(meta2);
				
				ItemStack item3 = new ItemStack(Material.FISHING_ROD);
				ItemMeta meta3 = item3.getItemMeta();
				meta3.setDisplayName("§eAngler");
				ArrayList<String> lore3 = new ArrayList<>();
				lore3.add("§bAusrüstung:");
				lore3.add("§e+ §7Angel");
				lore3.add("§e+ §7Boot");
				lore3.add("§e+ §7Fisch (+ Rückstoß 3)");
				lore3.add("");
				lore3.add("§7Nutzbar ab:");
				lore3.add("§a5000 Punkte");
				meta3.setLore(lore3);
				item3.setItemMeta(meta3);
				
				this.inv.setItem(0, item1);
				this.inv.setItem(1, item2);
				this.inv.setItem(2, item3);
				p.openInventory(this.inv);

				} else {
					p.sendMessage(Devathlon.prefix + "§cDie Statistiken sind zurzeit deaktiviert!");
				}
			}
			
		
		}
		
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();

			try {
				if (e.getInventory().getName().equalsIgnoreCase("§9Kits")) {
					
					// Kit 1
					if (e.getCurrentItem().getType() == Material.STONE_PICKAXE) {
						e.setCancelled(true);
						if (Global.re.readStats(StatsData.Punkte, p.getName(),p.getUniqueId().toString()) >= 1000) {
							if (Global.kit1.contains(p)) {
								Global.kit1.remove(p);
							}
							if (Global.kit2.contains(p)) {
								Global.kit2.remove(p);
							}
							if (Global.kit3.contains(p)) {
								Global.kit3.remove(p);
							}
							Global.kit1.add(p);
							p.sendMessage(Devathlon.prefix + "§aDu wirst im Spiel das Kit §eBergarbeiter §aerhalten");
							
						} else {
							p.sendMessage(Devathlon.prefix + "§cDu benötigst §e1000 §cPunkte um dieses Kit benutzen zu dürfen!");
						}
						p.closeInventory();
					}
					
					if (e.getCurrentItem().getType() == Material.STONE_AXE) {
						e.setCancelled(true);
						if (Global.re.readStats(StatsData.Punkte, p.getName(),p.getUniqueId().toString()) >= 2500) {
							if (Global.kit1.contains(p)) {
								Global.kit1.remove(p);
							}
							if (Global.kit2.contains(p)) {
								Global.kit2.remove(p);
							}
							if (Global.kit3.contains(p)) {
								Global.kit3.remove(p);
							}
							Global.kit2.add(p);
							p.sendMessage(Devathlon.prefix + "§aDu wirst im Spiel das Kit §eHolzfäller §aerhalten");
							
						} else {
							p.sendMessage(Devathlon.prefix + "§cDu benötigst §e2500 §cPunkte um dieses Kit benutzen zu dürfen!");
						}
						p.closeInventory();
					}
					
					if (e.getCurrentItem().getType() == Material.FISHING_ROD) {
						e.setCancelled(true);
						if (Global.re.readStats(StatsData.Punkte, p.getName(),p.getUniqueId().toString()) >= 5000) {
							if (Global.kit1.contains(p)) {
								Global.kit1.remove(p);
							}
							if (Global.kit2.contains(p)) {
								Global.kit2.remove(p);
							}
							if (Global.kit3.contains(p)) {
								Global.kit3.remove(p);
							}
							Global.kit2.add(p);
							p.sendMessage(Devathlon.prefix + "§aDu wirst im Spiel das Kit §eAngler §aerhalten");
							
						} else {
							p.sendMessage(Devathlon.prefix + "§cDu benötigst §e5000 §cPunkte um dieses Kit benutzen zu dürfen!");
						}
						p.closeInventory();
					}
				}
			} catch (Exception ex) {

			}
	}
	

}
