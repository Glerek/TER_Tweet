package TagTweet;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;


import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import utilitaire.MysqlTweet;

public final class GetFollowers {
    /**
     * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
     * on met le username de la personne 
     * getFollowersId : me donne la liste ds personnes qui suivent l'element passe en argument
     * getFriendId : me donne la liste des personnes que suit l'element mis en argument
     *
     * @param args message
     */
	public static void main(String[] args) {
		long search_ids[] = new long[50];

		try {
			MysqlTweet base = new MysqlTweet();
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey("1bbHLm0zQZUGgmeNetHLg")
					.setOAuthConsumerSecret(
							"DQwQ4QFS7vuKbRwjVNWIJGhx8cqT4XLW1UHO7ciwRk")
					.setOAuthAccessToken(
							"295594184-qP7k5jw26OJioN0Ks3WNSwWS2wz19IdjcabrML4J")
					.setOAuthAccessTokenSecret(
							"FnvCQGDIp0Nw9gp56A6LXjZ1RJkerhaQu0aMtUy9HNw");
			TwitterFactory tf = new TwitterFactory(cb.build());

			Twitter twitter = tf.getInstance();
			// pour avoir le nombre de followers d'une personne : showUser puis
			// getFollowersCount

			if ((args.length != 1) && (args.length != 2)) {
				System.out.println("Usage GetFollowers <id or screenName>");
				System.exit(1);
			}

			String nom_user = args[0];

			User u = twitter.showUser(nom_user);
			System.out.println(u.getName());// le nom

			System.out.println("followers count = "+u.getFollowersCount()); // pour francois hollande
														// c 175675 le
														// 27/02/2012
			System.out.println(u.getDescription());
			System.out.println("friends count = "+u.getFriendsCount()); // ceux que lui il suit
														// 1680
			System.out.println(u.getLocation()); // la localisation
			System.out.println(u.getScreenName()); // username sut twitter

			base.insertUser4j(u);

			// pour avoir la liste des followers

			long cursor = IDs.START;
			long new_cursor = IDs.START;
			IDs ids = null;
			int p = 0;

			System.out.println("Listing followers' ids.");

			if (args.length == 1) {
				do {
					cursor = new_cursor;
					ids = twitter.getFollowersIDs(nom_user, cursor);
					new_cursor = ids.getNextCursor();
					System.out.println("New Cursor = " + new_cursor);
				} while (new_cursor != 0);
				System.out
						.println("Le dernier cursor est "
								+ cursor
								+ " vous devriez relancer le programme avec GetFollowers "
								+ nom_user + " " + cursor);
//				System.exit(0);
			} else {

				cursor = Long.parseLong(args[1]);
			}
			
			System.out.println("valeur utilis√©e cursor = " + cursor);
			while (cursor != 0) {
				System.out.println("Cursor = " + cursor);
				ids = twitter.getFollowersIDs(nom_user, cursor);

				long tab_ids[] = ids.getIDs();
				int taille = tab_ids.length;
				System.out.println("nb de cette page = " + taille);

				Vector<Long> new_ids = new Vector<Long>();
				for (long id : ids.getIDs()) {
					if (!base.existInBase("users", "id=" + id)) {
						new_ids.add(id);
					}
				}
				System.out.println("nb d'utilisateur a ajouter = "
						+ new_ids.size());

				int i = 0;
				while (i < new_ids.size()) {
					int j;
					
					for (j = 0; (j < 50) && (i + j < new_ids.size()); j++) {
						search_ids[j] = new_ids.get(i + j);
					}
					System.out.println("attente");
					Thread.sleep(5000);
					System.out.println("fin");
					
					List<User> lu = twitter.lookupUsers(search_ids);
					Iterator<User> it = lu.iterator();
					while (it.hasNext()) {
						base.insertUser4j(it.next());
					}
					i += j;

					System.out.println("i=" + i);
				}

				for (i = 0; i < tab_ids.length; i++) {
					base.insertLink(tab_ids[i], u.getId());
				}

				cursor = ids.getPreviousCursor();
				System.out.println("Cursor = " + cursor + " p=" + p);
				p++;
				System.out.println("attente");
				Thread.sleep(4000);
				System.out.println("fin");
			}

			System.out.println("fiiiiiiiiiiiiiiiiiiiiiiiiiin" + p);

			// donc tte la liste est dans l

			// si on veut recuperer les tweets de ces followers :

			System.exit(0);
		} catch (Exception te) {
			te.printStackTrace();
			System.out.println("Failed to get followers' ids: "
					+ te.getMessage());
			int i;
			for (i=0; i<search_ids.length; i++) {
				System.out.println("id="+search_ids[i]);
			}
			System.exit(-1);
		}
	}
}