package de.abgecodet.devathlon.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsReader {
	
	/*
	 * Author: abgeFAQt
	 */
	
	public static PlayerDataClass read(String uuid){
		ResultSet rs = MySQL.query("SELECT uuid,Player,Toetungen, Tode, SpieleGewonnen, SpieleGespielt, Punkte FROM playerstats WHERE uuid='"+uuid+"'");
		try {
			if(rs.next()){
				PlayerDataClass pDC = new PlayerDataClass();
				pDC.playerName = rs.getString("Player");
				pDC.Toetungen = rs.getInt("Toetungen");
				pDC.Tode = rs.getInt("Tode");
				pDC.SpieleGewonnen = rs.getInt("SpieleGewonnen");
				pDC.SpieleGespielt = rs.getInt("SpieleGespielt");
				pDC.Punkte = rs.getInt("Punkte");
				return pDC;
			}
		} catch (Exception e) {

		}
		return null;
	}

	public static int readStats(StatsData data, String player, String uuid){
		ResultSet rs = MySQL.query("SELECT "+data.toString()+" FROM playerstats WHERE uuid='"+uuid+"'");
		int rückgabe = 0;
		try {
			if(rs.next()){
				rückgabe = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rückgabe;
	}
	
	
	public static class StatsWriter {
		//public static void addStats(Stats_Data data, String player,int add){
			//addStats(data,player,add,"");
		//}

		public static void write (PlayerDataClass pDC, String uuid){
			//uuid,Player,Toetungen, Tode, SpieleGewonnen, SpieleGespielt, Punkte
			try {
				MySQL.update("UPDATE playerstats SET Toetungen = "+pDC.Toetungen+", Tode = "+pDC.Tode+", SpieleGewonnen = "+pDC.SpieleGewonnen+", SpieleGespielt = "+pDC.SpieleGespielt+", Punkte = "+pDC.Punkte+"  WHERE uuid ='"+uuid+"'");				
			} catch (Exception e) {

			}

			
		}
		public static void addStats(StatsData data, String player,int add, String uuid){
			ResultSet rs = MySQL.query("SELECT "+data.toString()+" FROM playerstats WHERE uuid='"+uuid+"'");
			
			
			int rückgabe = 0;

			try {
				
				if(rs.next()){
					rückgabe = rs.getInt(1);
					rückgabe+=add;
					MySQL.update("UPDATE playerstats SET "+data.toString()+"='"+rückgabe+"' WHERE uuid ='"+uuid+"'");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		public static void removeStats(StatsData data, String player,int remove, String uuid){
			ResultSet rs = MySQL.query("SELECT "+data.toString()+" FROM playerstats WHERE uuid='"+uuid+"'");
			int rückgabe = 0;
			try {
				if(rs.next()){
					rückgabe = rs.getInt(1);
					rückgabe-=remove;
					MySQL.update("UPDATE playerstats SET "+data.toString()+"='"+rückgabe+"' WHERE uuid='"+uuid+"'");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
}

	public enum StatsData {

		Toetungen, Tode, SpieleGewonnen, SpieleGespielt, Punkte;

	}
}