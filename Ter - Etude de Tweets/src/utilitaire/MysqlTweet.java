package utilitaire;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import Tweets.SQLElement;






public class MysqlTweet {
	Statement myst;

	/**
	 * Constructeur sans argument
	 * @deprecated il faut le refaire avec des parametres
	 * @TODO am√©liorer ce constructeur
	 */
	public MysqlTweet() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		} catch (IllegalAccessException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		}
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tweets2?" +
					"user=tweets&password=4Wf5Y8ACTcBx9WZz");
			this.myst = connection.createStatement();
		} catch (SQLException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		}
	}

	/**
	 * Constructeur sans argument
	 * @param base la base √† utiliser
	 * @param user l'utilisateur pour la connexion
	 * @param pass son mot de passe
	 */
	public MysqlTweet(String base, String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		} catch (IllegalAccessException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		}
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/"+base+"?" +
					"user="+user+"&password="+pass);
			this.myst = connection.createStatement();
		} catch (SQLException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		}
	}
	
/*	public String getSimpleSaveInBaseString(Tweets.Tweet tw) {
		return tw.getSQLId();
	}
	*/
	
	public ResultSet execReq(String req) {
		try {
			//	    System.out.println("req = "+req);
			ResultSet rs = myst.executeQuery(req);
			return rs;
		}
		catch (SQLException ex) {
			System.out.println(ex);
			return null;
		}

	}


	public boolean existInBase(String base, String cond) {
		String req = "SELECT * "
				+ " FROM " + base
				+ " WHERE " + cond
				+ ";";
		try {
			//	    System.out.println("req = "+req);
			ResultSet rs = myst.executeQuery(req);
			if (rs.absolute(1)) {
				//		System.out.println("Il y a un r√©sultat");

				return true;
			} else {
				return false;
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
			return false;
		}

	}
	
	
	public Collection<Tweets.Tweet> getTweetFromWhere(String table, String cond) {
		Collection<Tweets.Tweet> res = new ArrayList<Tweets.Tweet>();
		String req = "SELECT * "
				+ " FROM " + table
				+ " WHERE " + cond
				+ ";";
		try {
			//	    System.out.println("req = "+req);
			ResultSet rs = myst.executeQuery(req);
			rs.beforeFirst();
			while(rs.next()) {
				Tweets.Tweet t = new Tweets.Tweet(
							rs.getString("id_tweet"),
							rs.getString("text"),
							rs.getString("fk_nom_user"),
							rs.getString("fk_id_user"),
							rs.getString("date_time"),
							rs.getString("source"),
							rs.getString("base64")
						); 
				res.add(t);
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
		}
				
		return res;
	}

	
	public Collection<Tweets.Tweet> getTweetFromRequest(String req) {
		Collection<Tweets.Tweet> res = new ArrayList<Tweets.Tweet>();

		try {
			//	    System.out.println("req = "+req);
			ResultSet rs = myst.executeQuery(req);
			rs.beforeFirst();
			while(rs.next()) {
				
				Tweets.Tweet t = new Tweets.Tweet(
							rs.getString("id_tweet"),
							rs.getString("text"),
							rs.getString("fk_nom_user"),
							rs.getString("fk_id_user"),
							rs.getString("date_time"),
							rs.getString("source"),
							rs.getString("base64")
						); 
				res.add(t);
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
		}
				
		return res;
	}
	public static String escape(String text) {
		String res;

		res = text.replaceAll("'", "''");
		String as = new String ("\\\\");
		String das = new String("\\\\\\\\"); 
		res = res.replaceAll(as, das);

		return res;
	}



	/**
	 * Pour ins√©rer un truc dans une table
	 * @param table la table de la pbase √† utilsier
	 * @param values les valeurs √† mettre (du style (champs1, champs2) VALUES (val1, val2)
	 * @deprecated voir insert(SQLElement);
	 */
	public void insert(String table, String values) {

		String req = "INSERT INTO " + table
				+ " " + values + ";";
		
		try {
			myst.executeUpdate(req);
		}
		catch (SQLException ex) {
			System.out.println("exception="+ex);
			System.out.println("req="+req);
			ex.printStackTrace();
		}

	}

	public void insertUser4j(twitter4j.User u) {
		long id = u.getId();
		String sn = "NULL";
		if (u.getScreenName() != null) {
			sn = "'"+MysqlTweet.escape(u.getScreenName())+"'";
		}
		String n = "NULL";
		if (u.getName() != null) {
			n = "'"+MysqlTweet.escape(u.getName())+"'";
		}
		String fo = "NULL";
		//	if (u.getFollowersCount() != null) {
		fo = "'"+u.getFollowersCount()+"'";
		//	}
		String fr = "NULL";
		//	if (u.getFriendsCount() != null) {
		fr = "'"+u.getFriendsCount()+"'";
		//	}
		String d = "NULL";
		if (u.getDescription() != null) {
			d = "'"+MysqlTweet.escape(u.getDescription())+"'";
		}
		String l = "NULL";
		if (u.getLocation() != null) {
			l = "'"+MysqlTweet.escape(u.getLocation())+"'";
		}
		if (!existInBase("users", "id_user='"+id+"'")) {
			String val = "(id_user, screen_name, name, nb_followers, nb_friends, description, location) "
					+"VALUES ("
					+id+","
					+sn+","
					+n+","
					+fo+","
					+fr+","
					+d+","
					+l
					+")";

			insert("users", val);
		}
	}

	public void insertLink(long id_f, long id_l) {

		if (!this.existInBase("user_follow_user", "(fk_id_follower='"+id_f+"') AND (fk_id_leader='"+id_l+"')")) {
			String vals = "(fk_id_follower, fk_id_leader) "
					+"VALUES ("
					+"'"+id_f+"',"
					+"'"+id_l+"'"
					+")";
			insert("user_follow_user", vals);
		}
	}

	
	/**
	 * Permet d'ins√©rer un tweet
	 * @param tweet le tweet √† ins√©rer sous la forme d'un tweet twiterforj
	 * @param origine l'origine du tweet (un mot clef pour classement futur)
	 * @deprecated cette m√©thode ne doit plus √™tre utilis√©e.
	 * @see Tweets.tweet.getInsertSql
	 * @return true si l'insertion a eu lieu et false sinon
	 */
	public boolean insertTweet4j(twitter4j.Tweet tweet, String origine) {
		String text = MysqlTweet.escape(tweet.getText());
		java.util.Date d = tweet.getCreatedAt();
		java.text.DateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String std = df1.format(d);

		if (!this.existInBase(Tweets.Tweet._SQL_table, "id_tweet='"+tweet.getId()+"'")) {

			String valeurs =
					"(id_tweet, fk_id_user, fk_nom_user, date_time, text, source, base64, origine) "
							+ "VALUES ("
							+ "'" + tweet.getId() + "',"
							+ "'" + tweet.getFromUserId() +"',"
							+ "'" + MysqlTweet.escape(tweet.getFromUser()) +"',"
							+ "'" + std +"',"
							+ "'" + text +"',"
							+ "'" + MysqlTweet.escape(tweet.getSource()) +"',"
							+ "'" + Chaine.toBase64String(tweet) +"',"
							+ "'" + MysqlTweet.escape(origine)+ "'"
							+ ")";
			this.insert(Tweets.Tweet._SQL_table, valeurs);
			return true;
		}
		return false;
	}

	
	/**
	 * @param tweet le tweet sous la forme tweetforj √† ins√©rer
	 * @deprecated cette m√©thode ne doit plus √™tre utilis√©e.
	 * @see Tweets.tweet.getInsertSql
	 * @return true si l'insertion a eu lieu et false sinon
	 */
	public boolean insertTweet4j(twitter4j.Tweet tweet) {
		return this.insertTweet4j(tweet, "candidat");
	}

	/**
	 * Pour sauvegarder un √©l√©ment
	 * @param elem l'√©l√©ment √† sauvegarder
	 */
	public void save(Tweets.SQLElement elem) {
		List<String> reqs = elem.getSQLSaveRequests();
		Iterator<String> it = reqs.iterator();
		String req = null;
		
		try {		
//			System.out.println(req);
			while(it.hasNext()) {
				req = it.next();
				myst.executeUpdate(req);
			}
		}
		catch (SQLException ex) {
			System.out.println("exception="+ex);
			System.out.println("req="+req);
			ex.printStackTrace();
		}
	}

	public static String SQLElementToSaveString(Tweets.SQLElement elem) {
		String res = "INSERT INTO ";
		res += elem.getSQLTable() +" ";
		res += elem.getSQLValuesName() + " ";
		res += "VALUES ";
		res += elem.getSQLValues() + " ";
		res += "ON DUPLICATE KEY UPDATE ";
		res += elem.getSQLOnDuplicate() + "; ";
		
		return res;
	}
	
	/**
	 * m√©thode pour initialiser un √©l√©ment SQL √† partir de la valeur de son identifiant
	 * cette fonction sera appel√©e par le constructeur de l'√©l√©ment en question 
	 * @param elem l'√©l√©ment √† initialiser
	 * @param id son identifiant
	 */
	public ResultSet getSqlValuesFromId(SQLElement elem, String id) {
		String req = "SELECT * FROM "+elem.getSQLTable()+
				" WHERE "+elem.getSQLIdName()+"='"+id+"'";
		try {
			ResultSet rs = myst.executeQuery(req);
			if (rs.absolute(1)) {
				// Il y a un √©l√©m√©en et un seul
				rs.next();
				return rs;
			} else {
				System.err.println("Il n'y a pas d'√©l√©ment d'identifiant "+id+" dans la table "+elem.getSQLTable());
			}
		}
		catch (SQLException ex) {
			System.out.println("Probl√®me lors de l'ex√©cution de la requete de cr√©ation");
			ex.printStackTrace();
		}
		return null;
	}

	
}