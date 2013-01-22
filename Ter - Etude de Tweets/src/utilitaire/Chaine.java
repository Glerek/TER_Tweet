package utilitaire;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import biz.source_code.base64Coder.Base64Coder;

public class Chaine {

	/** Write the object to a Base64 string. */
	public static String toBase64String( Serializable o ) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream( baos );
			oos.writeObject( o );
			oos.close();
			return new String( Base64Coder.encode( baos.toByteArray() ) );
		} catch(IOException e) {
			System.out.println(e);
		}
	
		return null;
	}

	/** Read the object from Base64 string. */
	public static Object fromBase64String( String s ) throws IOException ,
	ClassNotFoundException {
		byte [] data = Base64Coder.decode( s );
		ObjectInputStream ois = new ObjectInputStream( 
				new ByteArrayInputStream(  data ) );
		Object o  = ois.readObject();
		ois.close();
		return o;
	}

	public static String md5sum(String text) {
		byte[] uniqueKey = text.getBytes();
		byte[] hash      = null;
	
		try
		{
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new Error("No MD5 support in this VM.");
		}
	
		StringBuilder hashString = new StringBuilder();
		for (int i = 0; i < hash.length; i++)
		{
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1)
			{
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			}
			else
				hashString.append(hex.substring(hex.length() - 2));
		}
		return hashString.toString();
	}

	
	/** fonction pour concat√©ner un ensemble de chaine en les s√©parant avec
	 * un s√©parateur fix√© (√©quivalente de la fonction implode de php)
	 * @param tab le tableau des √©l√©ments √† ajouter √† la chaine
	 * @param sep le s√©parateur √† utiliser
	 * @return une chaine vide si le tableau est de taille null
	 */
	public static String implode(String [] tab, String sep) {
		String res = new String();
		int i;
		int nbm1 = tab.length-1;
		if (nbm1 >=0) {
			for (i=0; i<nbm1; i++) {
				res += tab[i]+sep;
			}
			res += tab[nbm1];
		}
		
		return res;
	}
	
}
