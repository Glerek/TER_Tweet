/**
 * Classe permettant d'ajouter les sujets pour touts les tweets existant.
 * Elle a √©t√© neutralis√©e en modifiant la fonction de requette utilis√©e
 */
package TagTweet;

import java.util.Collection;
import java.util.Iterator;

import utilitaire.TweetException;
import Tweets.SubjectList;
import Tweets.Tweet;

/**
 * @author frico
 *
 */
public class AjouteSujet {
	public static void main(String args[]) {
		utilitaire.MysqlTweet base = new utilitaire.MysqlTweet("tweets2", "tweets", "4Wf5Y8ACTcBx9WZz");
		
//		Collection<Tweet> list = base.getTweetFromWhere("tweets.tweets", "origine='autre' AND text LIKE '%fran√ßois%' LIMIT 0,100");
			
		SubjectList subs = new SubjectList();
		
		
		try {
			subs.addSubject("@NathalieArthaud");
			subs.addSubject("@bayrou");
			subs.addSubject("@dupontaignan");
			subs.addSubject("@fhollande");
			subs.addSubject("@evajoly");
			subs.addSubject("@MLP_officiel");
			subs.addSubject("@melenchon2012");
			subs.addSubject("@PhilippePoutou");
			subs.addSubject("@NicolasSarkozy");
			subs.addSubject("@Villepin");
			subs.addSubject("@corinnelepage");
			subs.addSubject("@Cheminade2012");

			subs.addExprToSubject("@NathalieArthaud", "Nathalie[\\s#@]*Arthaud");
			subs.addExprToSubject("@bayrou", "Fran√ßois[\\s#@]*Bayrou");
			subs.addExprToSubject("@dupontaignan", "Nicolas[\\s#@]*Dupont-Aignan");
			subs.addExprToSubject("@fhollande", "Fran√ßois[\\s#@]*Hollande");
			subs.addExprToSubject("@evajoly", "Eva[\\s#@]*Joly");
			subs.addExprToSubject("@MLP_officiel", "Marine[\\s#@]*Le[\\s#@]*Pen");
			subs.addExprToSubject("@melenchon2012", "Jean-Luc[\\s#@]*Melenchon");
			subs.addExprToSubject("@PhilippePoutou", "Philippe[\\s#@]*Poutou");
			subs.addExprToSubject("@NicolasSarkozy", "Nicolas[\\s#@]*Sarkozy");
			subs.addExprToSubject("@Villepin", "Dominique[\\s#@]*de[\\s#@]*Villepin");
			subs.addExprToSubject("@corinnelepage", "Corinne[\\s#@]*Lepage");
			subs.addExprToSubject("@Cheminade2012", "Jacques[\\s#@]*Cheminade");

			Tweet.setSubList(subs);
	
//			System.out.println(subs.parseSubject("Dans quelques min. retrouvez Fran√ßois @Bayrou en meeting √† #Strasbourg! Suivez en direct"));

			int nb = 1000;
			int nb_max = 1000;
			int deb = 0;
			
			while(nb == nb_max) {
//				Collection<Tweet> list = base.getTweetFromWhere("tweets.tweets", "1 ORDER BY date_time LIMIT "+deb+","+nb_max);
				Collection<Tweet> list = base.getTweetFromWhere("tweets.tweets", "0 ORDER BY date_time LIMIT "+deb+","+nb_max);

				nb = list.size();
				deb += nb;
				
				Iterator<Tweet> it = list.iterator();
				while (it.hasNext()) {
					Tweet t = it.next();
					System.out.println(t);
					System.out.println(t.getSubjects());
					base.save(t);
				}
				System.out.println("#######################");
				System.out.println("deb="+deb);
				System.out.println("#######################");
//				System.exit(0);
			}
		} catch (TweetException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
