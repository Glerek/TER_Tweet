/**
 * Classe g√©n√©rique pour toutes les annotations
 */
package Tweets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utilitaire.MysqlTweet;
import utilitaire.TweetException;

/**
 * @author frico
 *
 */
public abstract class Note implements SQLElement {
	/// nom de la table SQL associ√©
	public static String _SQL_table = "notes";
	public static String _SQL_ID_NAME = "id_note";
	
	/// String to represent automatic annotation's author
	public static String AUTOMATIC = "automatic";
	
	/// String used to represent subject type
	public static String TYPE_SUBJECT = "subject"; 
	/// String used to represent orientation
	public static String TYPE_ORIENTATION = "orientation";
	
	/// L'id dans la base
	protected String id;
	/// L'id du tweet correspondant
	protected String id_tweet;
	/// le type de l'annotation pour le moment 'orientation' et 'sujet'
	protected String type;
	/// la valeur String de l'annotation 
	protected String val_string;
	/// la valeur num de l'annotation
	protected int val_int;
	/// l'auteur de l'annotation
	protected String origin;
	
	/**
	 * initialisation g√©n√©rique de l'annotation
	 * @param id l'ID dans la base ou null
	 * @param type le type voir dans la BD
	 * @param val_string la valeur string de l'annotation
	 * @param val_int la valeur num de l'annotation
	 * @param origin l'auteur de l'annotation
	 */
	private void initialise(String id, String id_tweet, String type,
			String val_string, int val_int, String origin) {
		this.id          = id;
		this.id_tweet    = id_tweet;
		this.type        = type;
		this.val_string  = val_string;
		this.val_int     = val_int;
		this.origin     = origin;	
	}

	private void initialise(ResultSet rs) throws SQLException {
		this.initialise(rs.getString("id_note"), rs.getString("fk_id_tweet"),
				rs.getString("type"), rs.getString("val_string"),
				rs.getInt("val_int"), rs.getString("origin"));
	}
	
	/**
	 * Constructeur g√©n√©rique de l'annotation
	 * @param id l'ID dans la base ou null
	 * @param type le type voir dans la BD
	 * @param val_string la valeur string de l'annotation
	 * @param val_int la valeur num de l'annotation
	 * @param origine l'auteur de l'annotation
	 */
	public Note(String id, String id_tweet, String type, String val_string, int val_int, String origin) {
		this.initialise(id, id_tweet, type, val_string, val_int, origin);
	}
	
	public Note(ResultSet rs) throws SQLException {
		this.initialise(rs);
	}
	
	public Note(MysqlTweet base, String id_sql) throws utilitaire.TweetException {
		ResultSet rs = base.getSqlValuesFromId(this, id_sql);
		try {
			this.initialise(rs);
		} catch (SQLException e) {
			System.out.println("Pour une note L'identifiant "+id_sql+"provoque une erreur");
			e.printStackTrace();
			throw (new utilitaire.TweetException("Pour une note L'identifiant "+id_sql+"provoque une erreur", e));
		}
	}
	
	

	public static Collection<Note> getNotesFromSQL(ResultSet rs) throws SQLException, TweetException {
		ArrayList<Note> res = new ArrayList<Note>();
		
		while (rs.next()) {
			String type = rs.getString("type");
			if (type.equals(TYPE_SUBJECT)) {
				res.add(new NoteSubject(rs));
			} 
			else if (type.equals(TYPE_ORIENTATION)) {
				res.add(new NoteOrientation(rs));
			}
			else {
				throw (new TweetException("Tentative de cr√©ation d'une annotation de type "+type+" qui est inconnu"));
			}
		}
		
		return res;
	}
	

	/**
	 * to transform note into String, this is a generic m√©thod which should not be used
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Note [id=" + id + ", id_tweet=" + id_tweet + ", type=" + type
				+ ", val_string=" + val_string + ", val_int=" + val_int
				+ ", origin=" + origin + "]";
	}
	
	@Override
	public List<String> getSQLSaveRequests() {
		String req = utilitaire.MysqlTweet.SQLElementToSaveString(this);
		
		List<String> res = new ArrayList<String>();
		res.add(req);
		
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((id_tweet == null) ? 0 : id_tweet.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + val_int;
		result = prime * result
				+ ((val_string == null) ? 0 : val_string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (id_tweet == null) {
			if (other.id_tweet != null)
				return false;
		} else if (!id_tweet.equals(other.id_tweet))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (val_int != other.val_int)
			return false;
		if (val_string == null) {
			if (other.val_string != null)
				return false;
		} else if (!val_string.equals(other.val_string))
			return false;
		return true;
	}

	@Override
	public String getSQLTable() {
		return Note._SQL_table;
	}

	@Override
	public String getSQLId() {
		return this.id;
	}

	@Override
	public String getSQLIdName() {
		return _SQL_ID_NAME;
	}
	
	@Override
	public String getSQLValuesName() {
		return "(fk_id_tweet, origine, type, val_string, val_int)";
	}

	@Override
	public String getSQLValues() {
		String res = "("+ this.id_tweet +" "
				+", '"+ this.origin +"'"
				+", '"+ this.type +"'"
				+", '"+ this.val_string +"'"
				+", '"+ this.val_int +"'"
				+")";
		return res;
	}

	@Override
	public String getSQLOnDuplicate() {
		String res = "val_string=VALUES(val_string)"
				+ ", val_int=VALUES(val_int)"
				;
		
		return res;
	}
	
	
	
}
