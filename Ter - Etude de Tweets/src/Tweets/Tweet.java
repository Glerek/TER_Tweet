/**
 * Classe permettant de g√©rer un tweet.
 */
package Tweets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import utilitaire.MysqlTweet;
import utilitaire.TweetException;

/**
 * @author frico
 * 
 */
public class Tweet implements SQLElement {
	/// Name of table containing tweets
	public static String _SQL_table = "tweets";
	/// Name of field containing id in sql table
	public static String _SQL_id_name = "id_tweet";
	
	/// List of interesting sunjects
	static SubjectList sub_list = null;
	/// identifiant tweeter du tweet
	protected String id;
	/// Nom de l'auteur (screen name)
	protected String author_name;
	/// id de l'auteur
	protected String author_id;
	/// text du tweet
	protected String date_str;
	/// la date du tweet
	protected String text;
	/// source du tweet
	protected String source;
/*	/// Origine du tweet (mot clef permettant de reconnaitre la requete utilis√©e pour la collecte
	protected String origine;
	*/
	/// Encodage en base 64 du tweets twitter4j
	protected String base64;
	/// sujets du tweet
	protected Map<String, Collection<Note>> notes_by_types;
	
	
	/**
	 * Constructeur quand on a les donn√©es
	 * @param id l'identifiant du tweet (attention cela doit √™tre celui de twiter
	 * @param text le text du tweet
	 * @param nom_aut le screenname de l'auteur
	 * @param id_auteur l'id de l'auteur (sur twiter)
	 * @param date la date du tweet au forma AAAA-MM-DD hh:mm:ss .
	 * @param source la source du tweet
	 * @param base64 l'encodage en base64 du tweet original
	 */
	private void initialise(String id, String text, String nom_aut,
			String id_auteur, String date, String source, String base64) {
		this.id = id;
		this.text = text;
		this.author_name = nom_aut;
		this.author_id = id_auteur;
		this.date_str = date;
		this.source = source;
		this.base64 = base64;
		this.notes_by_types = new HashMap<String, Collection<Note>>();
	}
	
	/**
	 * Constructeur quand on a les donn√©es
	 * @param id l'identifiant du tweet (attention cela doit √™tre celui de twiter
	 * @param text le text du tweet
	 * @param nom_aut le screenname de l'auteur
	 * @param id_auteur l'id de l'auteur (sur twiter)
	 * @param date la date du tweet au forma AAAA-MM-DD hh:mm:ss .
	 * @param source la source du tweet
	 * @param base64 l'encodage en base64 du tweet original
	 */
	public Tweet(String id, String text, String nom_aut, String id_auteur, String date, String source, String base64) {
		this.initialise(id, text, nom_aut, id_auteur, date, source, base64);
	}

	
	public Tweet(MysqlTweet base, String id_sql) {
		ResultSet rs = base.getSqlValuesFromId(this, id_sql);
		
		try {
			this.initialise(rs.getString("id_tweet"), rs.getString("text"),
					rs.getString("fk_nom_user"), rs.getString("fk_id_user"),
					rs.getString("date_time"), rs.getString("source"),
					rs.getString("base64"));
			
			
		} catch (SQLException e) {
			System.out.println("Pour un Tweet L'identifiant "+id_sql+"provoque une erreur");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	public String toString() {
		return "Tweet [id=" + id + ", text=" + text + ", nom_auteur="
				+ author_name + ", date_str="
						+ date_str +"]";
	}
	
	public String getText() {
		return text;
	}

	public String getAuthorName() {
		return author_name;
	}

	public Collection<Note> getSubjects() throws TweetException {
		Collection<Note> subjects = this.notes_by_types.get(Note.TYPE_SUBJECT);
		if (subjects == null) {
			this.subjectComputation(null);
			return this.notes_by_types.get(Note.TYPE_SUBJECT);
		}
		return subjects;
	}
	
	public void subjectComputation(SubjectList sl) throws TweetException {
		Collection<Note>  subjects;
		if (sl != null) {
			subjects = sl.parseSubject(this.id, this.text);
		}
		else if (Tweet.sub_list != null) {
			subjects = Tweet.sub_list.parseSubject(this.id, this.text);
		} else {
			throw new utilitaire.TweetException("Impossible de calculer les sujets si la liste de sujet n'est pas fournie et si la liste par defaut n'est pas fix√©e");
		}
		this.notes_by_types.put(Note.TYPE_SUBJECT, subjects);
	}

	
	public static SubjectList getSubList() {
		return sub_list;
	}

	public static void setSubList(SubjectList sub_list) {
		Tweet.sub_list = sub_list;
	}

	

	/* (non-Javadoc)
	 * @see Tweets.SQLElement#getSQLTable()
	 */
	@Override
	public String getSQLTable() {
		return Tweet._SQL_table;
	}
	/* (non-Javadoc)
	 * @see Tweets.SQLElement#getSQLId()
	 */
	@Override
	public String getSQLId() {
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see Tweets.SQLElement#getSQLIdName()
	 */
	@Override
	public String getSQLIdName() {
		return _SQL_id_name;
	}
	
	/* (non-Javadoc)
	 * @see Tweets.SQLElement#getSQLValuesName()
	 */
	@Override
	public String getSQLValuesName() {
		return "("+_SQL_id_name+", fk_id_user, fk_nom_user, date_time, text, source, base64)";
	}

	/* (non-Javadoc)
	 * @see Tweets.SQLElement#getSQLValues()
	 */
	@Override
	public String getSQLValues() {
		return "("
				+ this.id          + ", "
				+ this.author_id   + ", "
				+ "'" + MysqlTweet.escape(this.author_name) + "', "
				+ "'" + MysqlTweet.escape(this.date_str)    + "', "
				+ "'" + MysqlTweet.escape(this.text) + "',"
				+ "'" + MysqlTweet.escape(this.source) + "',"
				+ "'" + MysqlTweet.escape(this.base64) + "'"
				+ ")";
	}

	/* (non-Javadoc)
	 * @see Tweets.SQLElement#getSQLOnDuplicate()
	 */
	@Override
	public String getSQLOnDuplicate() {
		return
				""
				+ "fk_id_user=VALUES(fk_id_user)"
				+ ", fk_nom_user=VALUES(fk_nom_user)"
				+ ", date_time=VALUES(date_time)"
				+ ", text=VALUES(text)"
				+ ", source=VALUES(source)"
				+ ", base64=VALUES(base64)"
				;
	}

	
	/**
	 * Renvoie la requete √† utiliser pour ins√©rer/updater
	 * @return une String
	 */
	@Override
	public List<String> getSQLSaveRequests() {
		String req = utilitaire.MysqlTweet.SQLElementToSaveString(this);
		List<String> res = new ArrayList<String>();
		res.add(req);
		
		Iterator<Collection<Note>> it = this.notes_by_types.values().iterator();
		while(it.hasNext()) {
			Iterator<Note> it2 = it.next().iterator();
			while (it2.hasNext()) {
				res.add(utilitaire.MysqlTweet.SQLElementToSaveString(it2.next()));
			}
		}
		
		
		return res;
	}




	

	
	

	
	
}
