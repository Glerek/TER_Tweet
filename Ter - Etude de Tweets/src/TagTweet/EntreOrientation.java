package TagTweet;

import utilitaire.Chaine;

public class EntreOrientation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] couples = {"sarkoactu#@NicolasSarkozy", 
				"StefanCavers#@MLP_officiel", 
				"hollandeactu#@fhollande", 
				"CyberManin#@NicolasSarkozy", 
				"sarkoactu#@fhollande", 
				"bayrouactu#@bayrou", 
				"nousbayrou#@bayrou", 
				"veritable_vote#@fhollande", 
				"veritable_vote#@NicolasSarkozy", 
				"akemoi#@fhollande", 
				"NicolasSarkozy#@NicolasSarkozy", 
				"veritable_vote#@evajoly", 
				"veritable_vote#@NathalieArthaud", 
				"veritable_vote#@PhilippePoutou", 
				"EmilyRoley#@NicolasSarkozy", 
				"veritable_vote#@dupontaignan", 
				"veritable_vote#@bayrou", 
				"TuxyJade#@NicolasSarkozy", 
				"veritable_vote#@corinnelepage", 
				"melenchon2012#@melenchon2012", 
				"tipilive#@NicolasSarkozy", 
				"MLP_officiel#@MLP_officiel", 
				"Toulous1#@bayrou", 
				"lah_ken#@NicolasSarkozy", 
				"chrisquette#@NicolasSarkozy", 
				"RamsesNefertary#@fhollande", 
				"louloubk2#@fhollande", 
				"jeunesse_ecolo#@evajoly", 
				"chang_hua#@bayrou"};
		
		String sql_list = "('"+Chaine.implode(couples, "','")+"')";
		
		String req = "SELECT t.id_tweet" +
				"FROM tweets AS t JOIN notes AS t ON t.id_tweet=n.fk_id_tweet " +
				"WHERE n.type='subject' " +
				"AND CONCAT(t.fk_nom_user, '#', n.val_string) IN "+sql_list +
				" ORDER BY RAND() LIMIT 0,100";
		
		
				
		
		
	}

}
