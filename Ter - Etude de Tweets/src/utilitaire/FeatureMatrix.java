package utilitaire;

import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;


/**
 * Shows up to 100 of the first retweets of a given tweet.

 */
public  class FeatureMatrix {
	String [] nom;
	HashMap<String, Integer> num_by_nom;
	HashMap<String, Integer[]> matrice;
	HashMap<String, Integer> nb_par_nom;

	public FeatureMatrix(HashMap<String, Integer> nb_par_nom) {
		int i;

		//	System.out.println("nb_par_nom : "+nb_par_nom.keySet());
		this.nom = nb_par_nom.keySet().toArray(new String[0]);
		this.nb_par_nom = nb_par_nom;
		this.matrice = new HashMap<String, Integer[]>();
		this.num_by_nom = new HashMap<String, Integer>();

		for (i=0; i<nom.length; i++) {
			System.out.println("nom[i]="+nom[i]);
			this.num_by_nom.put(nom[i], i);
		}
	}

	public void addWord(String nom, String mot) {
		if (!this.matrice.containsKey(mot)) {
			int i;
			Integer tab[] = new Integer[this.nom.length];
			for (i=0; i<tab.length; i++) {
				tab[i] = 0;
			}
			this.matrice.put(mot, tab);

		}

		// System.out.println(nom+" : " +mot);
		// System.out.println("num_by_nom "+num_by_nom);
		// System.out.println("mots "+this.matrice.get(mot));
		// System.out.println("num "+this.num_by_nom.get(nom));
		this.matrice.get(mot)[this.num_by_nom.get(nom)]++;

	}

	public void sauve(String fichier) {
		try{
			int i;

			FileWriter fich = new FileWriter(fichier);
			BufferedWriter out=new BufferedWriter(fich);
			Iterator<String> it = this.matrice.keySet().iterator();

			for (i=0; i<this.nom.length; i++) {
				out.write(";\""+this.nom[i]+"\"");
				//		System.out.println(this.nom[i]);
			}
			out.newLine();

			while (it.hasNext()) {
				String key = it.next();

				out.write("\""+key+"\"");
				for (i=0; i<this.matrice.get(key).length; i++) {
					out.write(";"+this.matrice.get(key)[i]);
				}
				out.newLine();
			}

			System.out.println("nom "+this.nom);
			System.out.println("nb_par_nom "+this.nb_par_nom);
			out.write("\"number\"");
			for (i=0; i<this.nom.length; i++) {
				out.write(";"+this.nb_par_nom.get(this.nom[i]));
			}
			out.newLine();

			out.close();
			fich.close();
		} catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}

}