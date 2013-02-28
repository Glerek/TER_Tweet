
import ReadXml.XmlConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import Analyse.*;
import java.io.IOException;
import java.sql.SQLException;
import org.annolab.tt4j.TreeTaggerException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kevinsancho
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TreeTaggerException, SQLException 
    {
        Statement my_st = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		} catch (IllegalAccessException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());
			System.exit(1);
		}
                XmlConnection xmlAction = new XmlConnection();
                xmlAction.parseXmlFile();
                /*System.out.println("Analyse de la phrase Test");
                TT4J tt = new TT4J("Test");*/
                /*Analyse ana = new Analyse();
                System.out.println("Update de la base des Mots Dieses");
                ana.updateHashtagBase();
                System.out.println("Update des evaluations des Mots Dieses");
                ana.updateHashtagRating();
                System.out.println("Update de la base des expressions");
                ana.updateExpressionBase();
                System.out.println("Update des evaluations des expressions");
                ana.updateExpressionRating();*/
                Analyse ana = new Analyse();
                ana.evaluateTweets("@bayrou #FH2012 Concours d'entrée ou épreuves d'admission pour les études universitaires.", 65789);
	}
    }
