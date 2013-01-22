/**
 * note class to store Subject of a tweet
 */
package Tweets;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author frico
 *
 */
public class NoteSubject extends Note {

	/**
	 * @param subject : subject of the tweets 
	 * @param author the author of the subjects affectation
	 */
	public NoteSubject(String id_tweet, String subject, String author) {
		super(null, id_tweet, Note.TYPE_SUBJECT, subject, 0, author);
	}

	/**
	 * Subject came from automatic computation
	 * @param subject : subject of the tweets 
	 */
	public NoteSubject(String id_tweet, String subject) {
		super(null, id_tweet, Note.TYPE_SUBJECT, subject, 0, Note.AUTOMATIC);
	}

	public NoteSubject(ResultSet rs) throws SQLException {
		super(rs);
	}

	@Override
	public String toString() {
		return "Sub["+ val_string +", "+id_tweet+"]";
	}
	
	
	

	
}
