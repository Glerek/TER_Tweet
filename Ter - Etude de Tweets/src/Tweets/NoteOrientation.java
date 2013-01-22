/**
 * Classe pour stocker les orientation de tweet
 */
package Tweets;

import java.sql.ResultSet;
import java.sql.SQLException;

import utilitaire.MysqlTweet;
import utilitaire.TweetException;

/**
 * @author frico
 *
 */
public class NoteOrientation extends Note {
	public static int _ORIENTATION_UNKNOWN = 0;
	public static int _ORIENTATION_POSITIVE = 1;
	public static int _ORIENTATION_NEGATIVE = -1;
	
	
	/**
	 * Constructeur
	 * @param id_tweet l'identifiant du tweet concern√©
	 * @param orientation l'orientation du tweet concernant le sujet _ORIENTATION_UNKNOWN,
	 * _ORIENTATION_POSITIVE ou _ORIENTATION_NEGATIVE
	 * @param origin l'origine de l'orientation
	 */
	public NoteOrientation(String id_tweet,
			int orientation, String origin) {
		super(null, id_tweet, Note.TYPE_ORIENTATION, null, orientation, origin);
	}

	/**
	 * @param rs
	 * @throws SQLException
	 */
	public NoteOrientation(ResultSet rs) throws SQLException {
		super(rs);
	}

	/**
	 * @param base
	 * @param id_sql
	 * @throws TweetException
	 */
	public NoteOrientation(MysqlTweet base, String id_sql)
			throws TweetException {
		super(base, id_sql);
	}

	@Override
	public String toString() {
		String orientation = null;
		
		if (this.val_int == _ORIENTATION_POSITIVE) {
			orientation = "positive";
		}
		else if (this.val_int == _ORIENTATION_NEGATIVE) {
			orientation = "negative";
		}
		else {
			orientation = "undefined";
		}

		return orientation+"("+this.origin+")";
	}

	
	
}
