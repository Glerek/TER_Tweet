package utilitaire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class TreeTaggerTweet {
	TreeTaggerWrapper<String> tt;
	ArrayList<TreeTaggerToken> res;
	
	public TreeTaggerTweet(String tree_tagger_home,
			String tree_tagger_model) throws IOException {
		this.res = new ArrayList<TreeTaggerToken>();
		System.setProperty("treetagger.home", tree_tagger_home);

		this.tt = new TreeTaggerWrapper<String>();
		this.tt.setModel(tree_tagger_model);

		this.tt.setHandler(new TokenHandler<String>()
				{
					public void token(String token,
								String pos,
								String lemma) {
								//System.out.println(token+"\t"+pos+"\t"+lemma);
								res.add(new TreeTaggerToken(token, pos, lemma));
					}
				}
		);
	} 

	String nettoie(String text) {
		text = text.replaceAll("http[^\\s]+", " ");
		text = text.replaceAll("\\\"", " ");
		text = text.replaceAll("[lL]'", "le ");
		text = text.replaceAll("[jJ]'", "je ");
		text = text.replaceAll("[cC]'", "ce ");
		text = text.replaceAll("[dD]'", "de ");
		text = text.replaceAll("[nN]'", "ne ");
		text = text.replaceAll("[qQ][uU]'", "que ");
		text = text.replaceAll("\\s+", " ");

		return text;
	}
	
	public List<TreeTaggerToken> getToken(String tw)
				throws IOException, TreeTaggerException {
		String text = this.nettoie(tw);
		ArrayList<String> val = new ArrayList<String>();
		text = text.replaceAll("[^\\p{L}-#@0-9]", " ");
		String tab[] = text.split(" ");

		int i;
		for (i=0; i<tab.length; i++) {
			val.add(tab[i]);
		}
//System.out.println(tw);
		res.clear();
		tt.process(val);

		return res;
	}

}
