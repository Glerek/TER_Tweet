package Tweets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import utilitaire.TweetException;

public class SubjectList {
	Hashtable<String, Collection<Pattern>> ereg_by_subject;
	
	/// constructeur d'une liste de sujet vide
	public SubjectList() {
		this.ereg_by_subject = new Hashtable<String, Collection<Pattern>>();
	}

	/**
	 * Ajoute un sujet √† la liste. Par defaut, le nom sert d'expression r√©guli√®re pour la recherche.
	 * @param name le nom du sujet √† ajouter
	 * @throws utilitaire.TweetException dans le cas ou le sujet existe d√©j√†, ainsi que dans le cas ou il est vide.
	 */
	public void addSubject(String name) throws utilitaire.TweetException {
		String reg = name;
		reg = reg.replaceFirst("^\\s", "");
		reg = reg.replaceFirst("\\s$", "");
		String key = reg;
		
		if (key == "") {
			throw new utilitaire.TweetException("Le sujet de nom "+name+" est impossible a utiliser");
		}
		
		Collection<Pattern> exprs = new ArrayList<Pattern>();
		exprs.add(Pattern.compile(".*"+reg+".*", Pattern.CASE_INSENSITIVE|Pattern.DOTALL));
		
		if (this.ereg_by_subject.get(key) != null) {
			throw new utilitaire.TweetException("Le sujet de nom "+name+" est d√©j√† pr√©sent dans la liste");
		}
		this.ereg_by_subject.put(key, exprs);
	}
	
	/**
	 * Ajoute 
	 * @param name le nom du sujet auquel ajouter l'expression
	 * @param expr l'expression √† ajouter
	 * @throws TweetException dans le cas ou le sujet n'est pas dans la liste
	 */
	public void addExprToSubject(String name, String expr) throws TweetException {
		Collection<Pattern> li = this.ereg_by_subject.get(name);
		if (li == null) {
			throw new utilitaire.TweetException("Le sujet de nom "+name+" n'est pas pr√©sent dans la liste");
		}
		
		li.add(Pattern.compile(".*"+expr+".*", Pattern.CASE_INSENSITIVE|Pattern.DOTALL));
	}
	
	public Collection<Note> parseSubject(String id_tweet, String s) {
		HashMap<String, Note> res  = new HashMap<String, Note>();
		
		Iterator<Map.Entry<String, Collection<Pattern>>> it = this.ereg_by_subject.entrySet().iterator();
		
		while(it.hasNext()) {
			Map.Entry<String, Collection<Pattern>> me = it.next();
			
			Iterator<Pattern> it_reg = me.getValue().iterator();
			Pattern regex;
			while (it_reg.hasNext()) {
				regex = it_reg.next();
//				System.out.println(regex+" : "+s);
				if (regex.matcher(s).matches()) {
//					System.out.println("match");
					Note n = new NoteSubject(id_tweet, me.getKey());
					res.put(me.getKey(), n);
				}
//				System.out.println();
			}		
		}
		
		return res.values(); 
	}
}
