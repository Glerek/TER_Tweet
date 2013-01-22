package TagTweet;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;

import Tweets.TextTweet;

import utilitaire.Lexique;
import utilitaire.MysqlTweet;

public class GetScore {

	public static void main(String[] args) {
		try {
			String req = null;
			if ((args.length == -1)
					||(args.length >1)
					) {
				System.err.println("Usage GetFeatureMatrix [requete sql]");
				System.exit(1);
			}
			if (args.length == 0) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-7);
				Date d = c.getTime();
				
//				d.setDate(d.getDate()-7);
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				req = "SELECT id,text FROM tweets WHERE origine LIKE 'autre%' "
						+"AND date_time>='"+df1.format(d)+"';";
				System.err.println("Pas de requete utilisateion de la requete par defaut : "+req);
			} else {
				req = args[0];
			}
			
			String tt_home = "/home/frico/Recherche/TopologicalLearning/Twitter/tree-tagger";
			String tt_model = "/home/frico/Recherche/TopologicalLearning/Twitter/tree-tagger/french-par-linux-3.2-utf8.bin";

			MysqlTweet base = new MysqlTweet();
			TextTweet tt = new TextTweet(tt_home, tt_model);
			Lexique lex = new Lexique("/home/frico/Recherche/TopologicalLearning/Twitter/AffectiveLexicon.csv");
			
			ResultSet res = base.execReq(req);

			ArrayList<String> t_noms = new ArrayList<String>();
			ArrayList<String> t_texts = new ArrayList<String>();

			while (res.next()) {
				t_noms.add(res.getString(1));
				t_texts.add(res.getString(2));
			}
			System.err.println(t_texts.size()+" tweets √† traiter");

			int i;
			for (i=0; i<t_texts.size(); i++) {
				String text = t_texts.get(i);

				double score = tt.getScore(text, lex);

				text = text.replaceAll("\\\"", " ");
				
				System.out.println(t_noms.get(i)+";\""+text+"\";"+score);
			}

		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}