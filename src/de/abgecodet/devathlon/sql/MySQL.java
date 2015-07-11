package de.abgecodet.devathlon.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import de.abgecodet.devathlon.core.Devathlon;

public class MySQL {
	
	/*
	 * Author: abgeFAQt
	 */
	
	public static Connection con;
	
	String host;
	String user;
	String passwort;
	String database;
	int port;
	
	public static Connection connect(String host, String user, String passwort, String database, int port) throws Exception{
		java.sql.Connection con = null;
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + passwort + "&autoReconnect=true");
		Bukkit.getConsoleSender().sendMessage(Devathlon.prefix + "MySQL Connect");
		MySQL.con = con;
		
		return con;
	}
	public static void update(String query){
		Statement st;
		try {
			st = (Statement) con.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ResultSet query(String query){
		ResultSet rs = null;
		
		Statement st;
		try {
			st = (Statement) con.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	public static void close() throws Exception{
		con.close();
	}

}