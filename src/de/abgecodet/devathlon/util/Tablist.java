package de.abgecodet.devathlon.util;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R2.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Tablist {
	
	/*
	 * Author: Creepah__
	 */
	
	private String header = "";
	private String footer = "";
	
	private PacketPlayOutPlayerListHeaderFooter packet = null;
	public Tablist() {
		
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public void setFooter(String footer) {
		this.footer = footer;
	}
	
	public void send(Player p) {
		if(header == null) header = "";
		if(footer == null) footer = "";
		header = ChatColor.translateAlternateColorCodes('&', header);
		header = header.replaceAll("%PLAYER%", p.getDisplayName());
		footer = ChatColor.translateAlternateColorCodes('&', footer);
		footer = footer.replaceAll("%PLAYER%", p.getDisplayName());
		
		PlayerConnection con = ((CraftPlayer)p).getHandle().playerConnection;
		
		IChatBaseComponent tabheader = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent tabfooter = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		packet = new PacketPlayOutPlayerListHeaderFooter(tabheader);
			
		try {
			Field f = packet.getClass().getDeclaredField("b");
			f.setAccessible(true);
			f.set(packet, tabfooter);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			con.sendPacket(packet);
		}
	}
	
	public void send(Player p, String header, String footer) {
		if(header == null) header = "";
		if(footer == null) footer = "";
		header = ChatColor.translateAlternateColorCodes('&', header);
		header = header.replaceAll("%PLAYER%", p.getDisplayName());
		footer = ChatColor.translateAlternateColorCodes('&', footer);
		footer = footer.replaceAll("%PLAYER%", p.getDisplayName());
		
		PlayerConnection con = ((CraftPlayer)p).getHandle().playerConnection;
		
		IChatBaseComponent tabheader = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent tabfooter = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		packet = new PacketPlayOutPlayerListHeaderFooter(tabheader);
			
		try {
			Field f = packet.getClass().getDeclaredField("b");
			f.setAccessible(true);
			f.set(packet, tabfooter);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			con.sendPacket(packet);
		}
	}
}
