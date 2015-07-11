package de.abgecodet.devathlon.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.abgecodet.devathlon.core.Devathlon;
import de.abgecodet.devathlon.core.GameStatus;
import de.abgecodet.devathlon.core.Global;
import de.abgecodet.devathlon.sql.PlayerDataClass;
import de.abgecodet.devathlon.sql.StatsReader.StatsWriter;
import de.abgecodet.devathlon.util.Title;

public class playerquitevent implements Listener {
	
	/*
	 * Author: abgeFAQt
	 */
	
	@EventHandler
	public void on(PlayerQuitEvent e){
		
		Player p = e.getPlayer();

		if (!(Global.spectator.contains(p.getName()))) {

			if (Devathlon.Status == GameStatus.Lobby | Devathlon.Status == GameStatus.Restarting) {
				e.setQuitMessage("");
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.sendMessage("§7< " + p.getDisplayName() + " §3hat das Spiel verlassen. (" + (Bukkit.getOnlinePlayers().size() - 1) + "/"+ Bukkit.getMaxPlayers() + ")");
				}
				if (Global.team1.contains(p)) {
					Global.team1.remove(p);
				}
				if (Global.team2.contains(p)) {
					Global.team2.remove(p);
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
				return;
			}
			
			if (Devathlon.Status == GameStatus.Ingame) {
				
				e.setQuitMessage("");
				
				if (Global.team1.contains(p)) {
					Global.team1.remove(p);
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.sendMessage("§7< " + p.getDisplayName() + " §3hat das Spiel verlassen.");
						all.sendMessage(Devathlon.prefix + "§cTeam Rot §7besitzt noch §e" + Global.team1.size() + " §7Spieler");				
					}
					PlayerDataClass pDCp = Global.re.read(p.getUniqueId().toString());
					pDCp.Tode +=1;
					StatsWriter.write(pDCp, p.getUniqueId().toString());
				}
				if (Global.team2.contains(p)) {
					Global.team2.remove(p);
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.sendMessage("§7< " + p.getDisplayName() + " §3hat das Spiel verlassen.");
						all.sendMessage(Devathlon.prefix + "§9Team Blau §7besitzt noch §e" + Global.team2.size() + " §7Spieler");				
					}
					PlayerDataClass pDCp = Global.re.read(p.getUniqueId().toString());
					pDCp.Tode +=1;
					StatsWriter.write(pDCp, p.getUniqueId().toString());
				}
				
				if (Global.team1.size() == 0) {
					Title wint2 = new Title("§9Team Blau", "§ahat gewonnen");

					for (Player all : Bukkit.getOnlinePlayers()) {
						all.sendMessage(Devathlon.prefix + "§9Team Blau §ahat das Spiel gewonnen!");
						all.playSound(all.getEyeLocation(), Sound.GHAST_DEATH, 2, 2);
						wint2.send(all);
					}
					for (Player t2 : Global.team2) {
						PlayerDataClass pDCt2 = Global.re.read(t2.getUniqueId().toString());
						pDCt2.SpieleGewonnen +=1;
						pDCt2.Punkte +=125;
						StatsWriter.write(pDCt2, t2.getUniqueId().toString());
						t2.sendMessage(Devathlon.prefix + "§e+ 125 Punkte");
					}
					
					Devathlon.Status = GameStatus.Restarting;
					Devathlon.Timer = 16;

					return;
				}
				
				if (Global.team2.size() == 0) {
					Title wint1 = new Title("§cTeam Rot", "§ahat gewonnen");
					
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.sendMessage(Devathlon.prefix + "§cTeam Rot §ahat das Spiel gewonnen!");
						wint1.send(all);
					}
					for (Player t1 : Global.team1) {
						PlayerDataClass pDCt1 = Global.re.read(t1.getUniqueId().toString());
						pDCt1.SpieleGewonnen +=1;
						pDCt1.Punkte +=125;
						StatsWriter.write(pDCt1, t1.getUniqueId().toString());
						t1.sendMessage(Devathlon.prefix + "§e+ 125 Punkte");
					}

					Devathlon.Status = GameStatus.Restarting;
					Devathlon.Timer = 16;
					
					return;
				}
			}

			
			
		} else {
			e.setQuitMessage("");
		}

	}
}		
	
