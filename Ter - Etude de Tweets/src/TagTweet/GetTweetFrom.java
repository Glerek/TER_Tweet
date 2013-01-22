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
public  class GetTweetFrom {


	public static void main(String[] args) {
		MysqlTweet base = new MysqlTweet();

		if ((args.length < 1)||(args.length > 3)||(args.length==2)) {
			System.out.println("Usage: java twitter4j.examples.tweets.GetRetweets <identifiant> [date1 date2]");
			System.exit(-1);
		}

		if (args.length == 1) {
			System.out.println("Showing up to 100 of the last tweet of [" + args[0] + "].");
		} else {
			System.out.println("Showing up to 100 of the tweet of [" + args[0] + "] since "+args[1]+" until "+args[2]+".");
		}
		try {
			TwitterFactory tf = new TwitterFactory();
			Twitter twitter = tf.getInstance();

			Query query = new Query("from:"+args[0]);
			query.rpp(100);
			if (args.length == 3) {
				query.since(args[1]);
				query.until(args[2]);
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