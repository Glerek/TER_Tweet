package utilitaire;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;


/**
 * Classe pour g√©rer les diff√©rents lexiques contenant des mots valu√©s.
 * @author frico
 * 
 */
public class Lexique {
	private HashMap<String, Double> mots;
	
	public Lexique(String nom_fich) {
		this.mots = new HashMap<String, Double>();
		int i = 0;
		try {
			java.io.FileReader f = new java.io.FileReader(nom_fich);
			java.io.BufferedReader buf = new java.io.BufferedReader(f);

			String line;
			i=0;
			while((line= buf.readLine()) != null) {
				i++;
				String tab[] = line.split(";");
				if (tab.length != 2) {
					throw new java.io.IOException("Le fichier "+nom_fich
							+" contient plus "+tab.length+" colonnes √† la ligne "+i);
				}
				double score = Double.parseDouble(tab[1]);
				if ((score <0)||(score>1)) {
					throw new java.io.IOException("Le fichier "+nom_fich
							+" contient un score hors de [0,1] :"+score+" √† la ligne "+i);
				}
				this.mots.put(tab[0].toLowerCase(), score);
			}		
		} catch (FileNotFoundException e) {
			System.err.println("Fichier "+nom_fich);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("Fichier "+nom_fich+" ligne "+i);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fichier "+nom_fich+" ligne "+i);
			e.printStackTrace();
		}
	}
	
	/**
	 * Pour obtenir le score du mot
	 * @param mot le mot dont on cherche le score
	 * @return une valeur Double comprise entre 0 (tr√®s n√©gatif) et 1 (tr√®s positif) ou null si le mot n'est pas dans le lexique
	 */
	public Double getPosValue(String mot) {
		if (this.mots.containsKey(mot)) {
			return this.mots.get(mot);
		} else {
			return null;
		}
	}
}
