package Tweets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import utilitaire.Lexique;
import utilitaire.TreeTaggerToken;
import utilitaire.TreeTaggerTweet;

import java.io.IOException;

import org.annolab.tt4j.TreeTaggerException;

public class TextTweet {
	TreeTaggerTweet tt;
	
	public TextTweet(String tt_home, String tt_model) {
		try {			
			this.tt = new TreeTaggerTweet(tt_home, tt_model);
		} catch (IOException e) {
			System.out.println("Impossible d'utiliser le treetagger"
						+" a partir de "+tt_home
						+" avec le model "+tt_model 
					);
			e.printStackTrace();
			System.exit(1);
		}
	}

	public List<String> getNGrame(String tw, int n) {
		int i, t;
		List<String> res = new ArrayList<String>();

		String text = tw.replaceAll("[\n]", ". ");
		text = text.replaceAll("[\"]", "_");

		t = text.length();

		for (i=0; i<t-n; i++) {
			res.add(text.substring(i, i+n));
		}

		return res;
	}

	public List<String> getPosLemma(String text) {
		try {
			List<TreeTaggerToken> l = this.tt.getToken(text);
			
			Iterator<TreeTaggerToken> it = l.iterator();
			
			List<String> res = new ArrayList<String>();
			
			while (it.hasNext()) {
				TreeTaggerToken ttk = it.next();
				res.add(ttk.pos+":"+ttk.lemma);
			}
			
			return res;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (TreeTaggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}

	public List<String> getLemma(String text) {
		try {
			List<TreeTaggerToken> l = this.tt.getToken(text);
			
			Iterator<TreeTaggerToken> it = l.iterator();
			
			List<String> res = new ArrayList<String>();
			
			while (it.hasNext()) {
				TreeTaggerToken ttk = it.next();
				res.add(ttk.lemma);
			}
			
			return res;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (TreeTaggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}

	public double sommeScores(ArrayList<Double> scores) {
		double score = 0;
		int i;
		
		if (scores.size()>1) {
			Double tab[] = scores.toArray(new Double[scores.size()]);
			Arrays.sort(tab);
			
			for (i=0; i<tab.length; i++) {
			//	score += tab[i]/(i+1);
				score += tab[i];
			}
		}
		
		return score;
	}
	
	public double getScore(String tweet, Lexique lex) {
		try {
			double score;
			int nb_mots;

			ArrayList<Double> scores_pos = new ArrayList<Double>();
			ArrayList<Double> scores_neg = new ArrayList<Double>();
			List<TreeTaggerToken> l = this.tt.getToken(tweet);
			Iterator<TreeTaggerToken> it = l.iterator();
			
			nb_mots = 0;
			while (it.hasNext()) {
				TreeTaggerToken ttt = it.next();
				Double val = lex.getPosValue(ttt.lemma);
				if (val != null) {
					if (val > 0.5) {
						scores_pos.add(val-0.5);
					} else {
						scores_neg.add(0.5-val);
					}
					nb_mots++;
//					System.out.println(ttt.lemma+";"+ttt.token+";"+val);
				}
			}
						
			if (nb_mots == 0) {
				return 0.5;
			}
			
			score = sommeScores(scores_pos)-sommeScores(scores_neg)+0.5;
			
			if (score >1) {
				score = 1;
			}
			if (score <0) {
				score = 0;
			}
			return score;
//			return score/nb_mots;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
		} catch (TreeTaggerException e) {
			
			e.printStackTrace();
			System.exit(1);
		}
		return -1;
	}
}

