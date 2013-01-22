package TagTweet;
import twitter4j.User;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import utilitaire.MysqlTweet;

import java.util.Iterator;

import java.util.List;


/**
 * Shows up to 100 of the first retweets of a given tweet.

 */
public  class GetTweetOn {
	Twitter twitter;
	MysqlTweet base;

	public GetTweetOn() {
		this.base = new MysqlTweet();

		//	System.out.println("ici");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("1bbHLm0zQZUGgmeNetHLg")
		.setOAuthConsumerSecret("DQwQ4QFS7vuKbRwjVNWIJGhx8cqT4XLW1UHO7ciwRk")
		.setOAuthAccessToken("295594184-qP7k5jw26OJioN0Ks3WNSwWS2wz19IdjcabrML4J")
		.setOAuthAccessTokenSecret("FnvCQGDIp0Nw9gp56A6LXjZ1RJkerhaQu0aMtUy9HNw");
		TwitterFactory tf = new TwitterFactory(cb.build());

		this.twitter = tf.getInstance();
		//System.out.println("twitter "+this.twitter);
		//System.out.println("ici");

	}

	public int traiteRequete(String req, int nb, String before, String origin) {
		int nbi = 0;        	
		Query query = null;
		QueryResult result = null;
		try {
			query = new Query(req);
			query.rpp(nb);

			if (before != "") {
				query.until(before);
			}

			//	    System.out.println("twitter "+this.twitter);
			//System.out.println("query "+query);

			result = this.twitter.search(query);

			java.util.List<Tweet> list =  result.getTweets();
			long search_ids[] = new long[nb];
			int i = 0;
			for (Tweet tweet : list) {
				search_ids[i++] = tweet.getFromUserId();
			}

			List<User> lu = this.twitter.lookupUsers(search_ids);
			Iterator<User> it = lu.iterator();
			while (it.hasNext()) {
				base.insertUser4j(it.next());
			}

			nbi=0;
			for (Tweet tweet : list) {
				// System.out.println(tweet.getFromUser() 
				// 		   + " : " + tweet.getText()
				// 		   + " : " + tweet.getCreatedAt() 
				// 		   + " : " + tweet.getGeoLocation() 
				// 		   //				   + " : " + tweet.getProfileImageUrl() 
				// 		   //				   + " : " + tweet.getSource() 
				// 		   + " : " + tweet.getId() 
				// 		   );
				if (base.insertTweet4j(tweet, origin)) {
					// System.out.println("A ete insere");
					nbi++;
				}		
			}
			//System.out.println(nbi+ "insertion done.");
			return nbi;
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed execute : " + te.getMessage());
			return nbi; 
		}

	}

	public static void main(String[] args) {
		int nbi;
		String before;

		if (args.length > 1) {
			System.out.println("Usage: java twitter4j.examples.tweets.GetRetweets [date]");
			System.exit(-1);
		}

		if (args.length == 1) {
			System.out.println("Showing up to 100 of the tweet before - [" + args[0] + "].");
			before = args[0];
		} else {
			System.out.println("Showing up to 100 of the last tweet.");
			before = "";
		}

		//	System.out.println("Coucou");
		GetTweetOn gto = new GetTweetOn();
		//	System.out.println("Coucou");

		String req;

		// ###############################################
		// ##
		// ##        Tweet contenant les id
		// ##
		// ###############################################

		req = "@NathalieArthaud "
				+ " OR @bayrou "
				+ " OR @dupontaignan "
				+ " OR @fhollande "
				+ " OR @evajoly "
				+ " OR @MLP_officiel "
				+ " OR @melenchon2012 "
				+ " OR @PhilippePoutou "
				+ " OR @NicolasSarkozy "
				+ " OR @Villepin "
				+ " OR @corinnelepage "
				+ " OR @Cheminade2012 "
				;
		nbi = gto.traiteRequete(req, 100, before, "autre");
		System.out.println(nbi + "tweets d'origine autre");


		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ###############################################
		// ##
		// ##        Tweet contenant les nom
		// ##
		// ###############################################
		req = "(Nathalie Arthaud) "
				+ " OR (Fran√ßois Bayrou) "
				+ " OR (Nicolas Dupont-Aignan) "
				+ " OR (Fran√ßois Hollande) "
				+ " OR (Eva Joly) "
				+ " OR (Marine Le Pen) "
				+ " OR (Jean-Luc Melenchon) "
				+ " OR (Philippe Poutou) "
				+ " OR (Nicolas Sarkozy) "
				+ " OR (Dominique de Villepin) "
				+ " OR (Corinne Lepage) "
				+ " OR (Jacques Cheminade) "
				;

		nbi = gto.traiteRequete(req, 100, before, "autre_nom");
		System.out.println(nbi + "tweets d'origine autre_nom");

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ###############################################
		// ##
		// ##   Tweet contenant les id et les emoticones
		// ##
		// ###############################################
		req = "(@NathalieArthaud "
				+ " OR @bayrou "
				+ " OR @dupontaignan "
				+ " OR @fhollande "
				+ " OR @evajoly "
				+ " OR @MLP_officiel "
				+ " OR @melenchon2012 "
				+ " OR @PhilippePoutou "
				+ " OR @NicolasSarkozy "
				+ " OR @Villepin "
				+ " OR @corinnelepage "
				+ " OR @Cheminade2012)"
				+ " AND (:( OR :))"
				;

		nbi = gto.traiteRequete(req, 100, before, "emoticon");
		System.out.println(nbi + "tweets d'origine emoticon");


		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ###############################################
		// ##
		// ##   Tweet contenant les id et les emoticones 
		// ##     et les nom
		// ##
		// ###############################################

		req = "((Nathalie Arthaud) "
				+ " OR (Fran√ßois Bayrou) "
				+ " OR (Nicolas Dupont-Aignan) "
				+ " OR (Fran√ßois Hollande) "
				+ " OR (Eva Joly) "
				+ " OR (Marine Le Pen) "
				+ " OR (Jean-Luc Melenchon) "
				+ " OR (Philippe Poutou) "
				+ " OR (Nicolas Sarkozy) "
				+ " OR (Dominique de Villepin) "
				+ " OR (Corinne Lepage) "
				+ " OR (Jacques Cheminade)) "
				+ " AND (:( OR :)) "
				;
		nbi = gto.traiteRequete(req, 100, before, "emoticon_nom");
		System.out.println(nbi + "tweets d'origine emoticon_nom");

		System.exit(0);
	}
}