
import ReadXml.XmlConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import Analyse.*;
import BDD.Query;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
                TT4J tt = new TT4J("/!\\ CETTE VIDÉO VA TE CONVAINCRE DE NE PAS VOTER SARKOZY http://t.co/6LV9nlC5… LE CHANGEMENT, c'est MAINTENANT, AVEC @fhollande! #FH2012!");*/
                Analyse ana = new Analyse();
                System.out.println("Update de la base des Mots Dieses");
                ana.updateHashtagBase();
                System.out.println("Update des evaluations des Mots Dieses");
                ana.updateHashtagRating();
                System.out.println("Update de la base des expressions");
                ana.updateExpressionBase();
                System.out.println("Update des evaluations des expressions");
                ana.updateExpressionRating();
                /*Analyse ana = new Analyse();
                //ana.evaluateTweets("@bayrou #FH2012 Concours d'entrée ou épreuves d'admission pour les études universitaires.", 65789);
                //ana.evaluateTweets("Tweeter votre vote pour Nathalie #Arthaud http://t.co/gjxFzTxI Election Presidentielle 2012 sur Twitter #FH2012 #Joly #placeaupeuple #MLP", "179046816136970240");*/
                
                Query query = new Query();
                String req = "select id_tweet, text from tweets where analysed = 0 order by rand()";
                ResultSet results = query.sendQuery(req);
                
                while(results.next()) {
                    System.out.println("Evaluation du Tweet "+results.getString(1));
                    ana.evaluateTweets(results.getString(2), results.getString(1));
                }
                
	}
    }
