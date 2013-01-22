package TagTweet;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import Tweets.TextTweet;

import utilitaire.FeatureMatrix;
import utilitaire.MysqlTweet;
import utilitaire.Utilitaire;

public class GetFeatureMatrix {

	public static void main(String[] args) {
		try {
			String req = null;
			if ((args.length == 0)
					||(args.length >2)
					||(
							(args[0].equals("token"))
							&&(args[0].equals("trigrame"))
							)
					) {
				System.err.println("Usage GetFeatureMatrix <token|trigrame> [requete sql]");
				System.exit(1);
			}
			if (args.length == 1) {
				req = "SELECT fk_nom_user, text FROM tweets WHERE origine='candidat'";

			} else {
				req = args[1];
			}
			String tt_home = "/home/frico/Recherche/TopologicalLearning/Twitter/tree-tagger";
			String tt_model = "/home/frico/Recherche/TopologicalLearning/Twitter/tree-tagger/french-par-linux-3.2-utf8.bin";

			MysqlTweet base = new MysqlTweet();
			TextTweet tt = new TextTweet(tt_home, tt_model);


			

			


			//	    System.out.println(req);

			//	    System.exit(1);

			ResultSet res = base.execReq(req);

			ArrayList<String> t_noms = new ArrayList<String>();
			ArrayList<String> t_texts = new ArrayList<String>();

			while (res.next()) {
				t_noms.add(res.getString(1));
				t_texts.add(res.getString(2));
			}

			//	    int nb_tweets = t_noms.size();

			HashMap<String, Integer> nb_tweets_par_nom = Utilitaire.factorLevels(t_noms);

			//	    String[] noms = nb_tweets_par_nom.keySet().toArray(new String[0]);
			// int i=0;
			// noms[i++] = "NathalieArthaud";
			// noms[i++] = "bayrou";
			// noms[i++] = "dupontaignan";
			// noms[i++] = "fhollande";
			// noms[i++] = "evajoly";
			// noms[i++] = "MLP_officiel";
			// noms[i++] = "melenchon2012";
			// noms[i++] = "PhilippePoutou";
			// noms[i++] = "NicolasSarkozy";
			// noms[i++] = "Villepin";
			// noms[i++] = "corinnelepage";

			FeatureMatrix mat = new FeatureMatrix(nb_tweets_par_nom);
			int i;

			for (i=0; i<t_texts.size(); i++) {
				//		System.out.println(text);
				String text = t_texts.get(i);
				String nom = t_noms.get(i);


				//		List<String> trigs = tt.getNGrame(text, 3);
				List<String> trigs = null;

				if (args[0].equals("token")) {
					trigs = tt.getPosLemma(text);
				} else {
					trigs = tt.getNGrame(text, 3);
				}

				Iterator<String> it = trigs.iterator();
				while (it.hasNext()) {
					String tri = it.next();
					//		    System.out.println(tri);		    
					mat.addWord(nom, tri);
				}

			}

			mat.sauve("matrice.csv");  
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}