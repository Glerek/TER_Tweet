package TagTweet;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import utilitaire.MysqlTweet;

/**
 * Shows up to 100 of the first retweets of a given tweet.

 */
public  class GetTweet {


	public static void main(String[] args) {
		MysqlTweet base = new MysqlTweet();

		if (args.length > 1) {
			System.out.println("Usage: java twitter4j.examples.tweets.GetRetweets [date]");
			System.exit(-1);
		}

		if (args.length == 1) {
			System.out.println("Showing up to 100 of the tweet before - [" + args[0] + "].");
		} else {
			System.out.println("Showing up to 100 of the last tweet.");
		}
		try {
			// ConfigurationBuilder cb = new ConfigurationBuilder();
			// cb.setDebugEnabled(true)
			// 	.setOAuthConsumerKey("1bbHLm0zQZUGgmeNetHLg")
			// 	.setOAuthConsumerSecret("DQwQ4QFS7vuKbRwjVNWIJGhx8cqT4XLW1UHO7ciwRk")
			// 	.setOAuthAccessToken("295594184-qP7k5jw26OJioN0Ks3WNSwWS2wz19IdjcabrML4J")
			// 	.setOAuthAccessTokenSecret("FnvCQGDIp0Nw9gp56A6LXjZ1RJkerhaQu0aMtUy9HNw");
			// TwitterFactory tf = new TwitterFactory(cb.build());
			TwitterFactory tf = new TwitterFactory();
			Twitter twitter = tf.getInstance();

			Query query = new Query( "from:NathalieArthaud "
					+ " OR from:bayrou "
					+ " OR from:dupontaignan "
					+ " OR from:fhollande "
					+ " OR from:evajoly "
					+ " OR from:MLP_officiel "
					+ " OR from:melenchon2012 "
					+ " OR from:PhilippePoutou "
					+ " OR from:NicolasSarkozy "
					+ " OR from:Villepin "
					+ " OR from:corinnelepage "
					+ " OR from:Cheminade2012 "
					);
			query.rpp(100);

			if (args.length == 1) {
				query.until(args[0]);
			}
			QueryResult result = twitter.search(query);

			java.util.List<Tweet> list =  result.getTweets();
			for (Tweet tweet : list) {
				System.out.println(tweet.getFromUser() 
						//				   + " : " + tweet.getText()
						+ " : " + tweet.getCreatedAt() 
						+ " : " + tweet.getGeoLocation() 
						//				   + " : " + tweet.getProfileImageUrl() 
						//				   + " : " + tweet.getSource() 
						+ " : " + tweet.getId() 
						);

				if (base.insertTweet4j(tweet, "candidat")) {
					System.out.println("Doit √™tre ins√©r√©");
				}

			}

			//	    System.out.println("done.");
			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get retweeted user ids: " + te.getMessage());
			System.exit(-1);
		}
	}
}