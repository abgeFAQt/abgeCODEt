package de.abgecodet.devathlon.util;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagManager {

	/*
	 * Author: Creepah__
	 */
	
	protected Player who;
	protected String tag;
	protected Collection<? extends Player> receivers;
	
	/**
	 * With the TagManager-Class you can changes players tags.
	 * @author Creepah__
	 * @param who Player which's tag should be changed.
	 */
	public TagManager(Player who) {
		this.who = who;
		this.receivers = Bukkit.getOnlinePlayers();
	}

	/**
	 * Change the tag that the player should get.
	 * @param tag The tag the player should get.
	 */
	public void changeTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * Get the tag of a player by their permissions.
	 */
	public String getTagByPermissions() {
		if(who.isOp()) {
			return "§4";
		}
		/* Permissions können hier gesetzt werden,
		 * um Gruppen eine andere Farbe (anderen Tag) zu geben
		 */
		return "§a";
	}
	
	/**
	 * Change the players which should see the tag.
	 * @param receivers The players which should see the tag.
	 */
	public void changeReceivers(Collection<? extends Player> receivers) {
		this.receivers = receivers;
	}
	
	/**
	 * Set the tag (final step)
	 */
	public void setTag() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Team team = board.getTeam(who.getName());
		if(team == null) {
			team = board.registerNewTeam(who.getName());
		}
		team.setPrefix(tag);
		team.addPlayer(who);
		for(Player players : receivers) {
			players.setScoreboard(board);
		}
		who.setDisplayName(getTagByPermissions() + who.getDisplayName()); 
	}
	
}
