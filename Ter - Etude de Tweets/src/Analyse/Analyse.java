/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import BDD.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.annolab.tt4j.TreeTaggerException;
/**
 *
 * @author Glerek
 */
public class Analyse {
    
    public Analyse() {                                                  
        /*req = "SELECT * FROM hashtag ORDER BY nb_occurence DESC LIMIT 0 , 5";

        results = my_st.executeQuery(req);

        while(results.next()) {
            //System.out.println(results.getString(2));
            my_st1 = connection.createStatement();
            req = "SELECT * FROM tweets WHERE rand() > 0.9 AND text LIKE '%"+results.getString(2)+"%' ORDER BY rand() LIMIT 0, 10";
            results1 = my_st1.executeQuery(req);
            score = 0;

            while(results1.next()) {
                System.out.println(results1.getString(5));
                //Ici on va regarder si l'auteur de chaque tweet est orienté et noter un score (+ ou -)
            }
        }*/
    }
    
    private static ArrayList<String> returnHashtags(String txt) {
        int nbOc = 0;
        int posOc = 0;
        int posNextSpace;
        ArrayList<String> result = new ArrayList<>();        
        
        while(txt.indexOf("#", posOc) != -1) {            
            posOc = txt.indexOf("#", posOc) +1;
            posNextSpace = txt.indexOf(" ",posOc);
            if(posNextSpace == -1) {
                posNextSpace = txt.length();
            }
            result.add(txt.substring(posOc, posNextSpace));
            nbOc++;
        }
        return result;
    }
    
    private static String cleanString(String txt) {
        int posOc = 0;
        int posNextSpace;
        String before;
        String after;
        
        //On vire les hashtags
        while(txt.indexOf("#") != -1) {            
            posOc = txt.indexOf("#", posOc);
            posNextSpace = txt.indexOf(" ",posOc);
            if(posNextSpace == -1) {
                posNextSpace = txt.length();
            }
            else {
                posNextSpace++;
            }
            before = txt.substring(0,posOc);
            after = txt.substring(posNextSpace,txt.length());
            txt = before+after;
        }
        
        posOc = 0;
        posNextSpace = 0;
        
        //On vire les liens
        while(txt.indexOf("http") != -1) {            
            posOc = txt.indexOf("http", posOc);
            posNextSpace = txt.indexOf(" ",posOc);
            if(posNextSpace == -1) {
                posNextSpace = txt.length();
            }
            else {
                posNextSpace++;
            }
            before = txt.substring(0,posOc);
            after = txt.substring(posNextSpace,txt.length());
            txt = before+after;
        }
        
        posOc = 0;
        posNextSpace = 0;
        
        //On vire les RTs
        while(txt.indexOf("RT ") != -1) {            
            posOc = txt.indexOf("RT ", posOc);
            posNextSpace = txt.indexOf(" ",posOc);
            if(posNextSpace == -1) {
                posNextSpace = txt.length();
            }
            else {
                posNextSpace++;
            }
            before = txt.substring(0,posOc);
            after = txt.substring(posNextSpace,txt.length());
            txt = before+after;
        }
        
        posOc = 0;
        posNextSpace = 0;
        
        //On vire les mentions
        while(txt.indexOf("@") != -1) {            
            posOc = txt.indexOf("@", posOc);
            posNextSpace = txt.indexOf(" ",posOc);            
            if(posNextSpace == -1) {
                posNextSpace = txt.length();
            }
            else {
                posNextSpace++;
            }
            before = txt.substring(0,posOc);
            after = txt.substring(posNextSpace,txt.length());
            txt = before+after;
        }        
        return txt;
    }
    
    public void updateHashtagBase() throws SQLException {
        Query query = new Query();
        String req, nomHashtag;
        ResultSet results, results1;
        int nbTweets, limitMin = 0, limitMax = 1000;
        Iterator it;
        ArrayList<String> listeTweet;
        
        req = "UPDATE tweets SET analysed = 1 WHERE text NOT LIKE '%#%'";
        query.sendUpdate(req);
        req = "SELECT COUNT(*) FROM tweets WHERE analysed = 0";
        results = query.sendQuery(req);
        results.next();
        nbTweets = results.getInt(1);

        for(int i=0;i<(Math.round(nbTweets/limitMax)+1);i++) {
            req = "SELECT text, id_tweet FROM tweets WHERE analysed = 0 LIMIT "+limitMin+", "+limitMax;
            results = query.sendQuery(req);

            while (results.next()) {
                    listeTweet = returnHashtags(results.getString(1));
                    it = listeTweet.iterator();

                    while(it.hasNext()) {
                        nomHashtag = (String) it.next();
                        req = "SELECT COUNT(*) FROM hashtag WHERE hashtag = '"+nomHashtag+"'";
                        results1 = query.sendQuery(req);
                        results1.next();
                        if(results1.getInt(1) == 0) {
                            req = "INSERT INTO hashtag VALUES(NULL,'"+nomHashtag+"',1)";
                        }
                        else {
                            req = "UPDATE hashtag SET nb_occurence = nb_occurence + 1 WHERE hashtag = '"+nomHashtag+"'";
                        }
                        query.sendUpdate(req);
                        query.sendUpdate("UPDATE tweets SET analysed = 1 WHERE id_tweet = "+results.getString(2));
                    }
            }
        }
    }
    
    public void updateHashtagRating() throws SQLException {
        String req, nomHashtag;
        ResultSet results, results1;
        Query query = new Query();
        Iterator it;
        int idHash;
        
        req = "SELECT * FROM evaluation WHERE hashtag_analysed = 0";                        
        results = query.sendQuery(req);

        while (results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            String eval = results.getString(4);
            
            req = "UPDATE evaluation SET hashtag_analysed = 1 WHERE id_evaluation = "+results.getInt(1);
            query.sendUpdate(req);

            req = "SELECT text FROM tweets WHERE id_tweet = "+id_tweet;
            results1 = query.sendQuery(req);
            results1.next();
            ArrayList<String> listeHash = returnHashtags(results1.getString(1));
            it = listeHash.iterator();

            while(it.hasNext()) {
                nomHashtag = (String) it.next();
                //On recupere l'id
                req = "SELECT id_hashtag FROM hashtag WHERE hashtag = '"+nomHashtag+"'";
                results1 = query.sendQuery(req);
                results1.next();
                idHash = results1.getInt(1);

                //On verifie que le hashtag est deja present dans la base
                req = "SELECT COUNT(*) FROM hashtag_rating WHERE fk_id_hashtag = "+idHash;
                results1 = query.sendQuery(req);
                results1.next();
                if(results1.getInt(1)==0) {
                    req = "INSERT INTO hashtag_rating VALUES (NULL,"+idHash+",0,0,0,0)";
                    query.sendUpdate(req);
                }

                switch(eval) {
                    case "against":
                        req = "UPDATE hashtag_rating SET nb_against = nb_against + 1 WHERE fk_id_hashtag = "+idHash;
                        break;
                    case "favorable":
                        req = "UPDATE hashtag_rating SET nb_favorable = nb_favorable + 1 WHERE fk_id_hashtag = "+idHash;
                        break;
                    case "neutral":
                        req = "UPDATE hashtag_rating SET nb_neutral = nb_neutral + 1 WHERE fk_id_hashtag = "+idHash;
                        break;
                    case "cantSay":
                        req = "UPDATE hashtag_rating SET nb_cantSay = nb_cantSay + 1 WHERE fk_id_hashtag = "+idHash;
                        break;
                }
                query.sendUpdate(req);
            }
        }
    }
    
    public void updateExpressionBase() throws SQLException, IOException, TreeTaggerException {
        String req;
        ResultSet results, results1;
        Iterator it;
        Query query = new Query();
        
        req = "SELECT * FROM evaluation WHERE expression_analysed = 0";                        
        results = query.sendQuery(req);

        while (results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            
            req = "UPDATE evaluation SET expression_analysed = 1 WHERE id_evaluation = "+results.getInt(1);
            query.sendUpdate(req);

            req = "SELECT text FROM tweets WHERE id_tweet = "+id_tweet;
            results1 = query.sendQuery(req);
            results1.next();
            TT4J tt = new TT4J(cleanString(results1.getString(1)));
            ArrayList<String> listLemma = tt.getLemma();
            it = listLemma.iterator();

            while(it.hasNext()) {
                String lemma = (String) it.next();
                //On ajout l'expression dans la base s'il n'existe pas
                req = "SELECT COUNT(*) FROM expression WHERE expression = '"+lemma+"'";
                results1 = query.sendQuery(req);
                results1.next();
                if(results1.getInt(1)==0) {
                    req = "INSERT INTO expression VALUES (NULL,'"+lemma+"',0)";
                    query.sendUpdate(req);
                }
                req = "UPDATE expression SET nb_occurence = nb_occurence +1 WHERE expression = '"+lemma+"'";
                query.sendUpdate(req);
            }
        }
    }
    
    public void updateExpressionRating() throws SQLException, IOException, TreeTaggerException {
        String req, nomExpression;
        ResultSet results, results1;
        Iterator it;
        int idHash;
        Query query = new Query();
        
        req = "SELECT * FROM evaluation WHERE expression_rating_analysed = 0";
                        
        results = query.sendQuery(req);

        while (results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            String eval = results.getString(4);
            
            req = "UPDATE evaluation SET expression_rating_analysed = 1 WHERE id_evaluation = "+results.getInt(1);
            query.sendUpdate(req);

            req = "SELECT text FROM tweets WHERE id_tweet = "+id_tweet;
            results1 = query.sendQuery(req);
            results1.next();
            TT4J tt = new TT4J(cleanString(results1.getString(1)));
            ArrayList<String> listLemma = tt.getLemma();
            it = listLemma.iterator();

            while(it.hasNext()) {
                nomExpression = (String) it.next();
                //On recupere l'id
                req = "SELECT id_expression FROM expression WHERE expression = '"+nomExpression+"'";
                results1 = query.sendQuery(req);
                results1.next();
                idHash = results1.getInt(1);

                //On verifie que l'expression est deja presente dans la base
                req = "SELECT COUNT(*) FROM expression_rating WHERE fk_id_expression = "+idHash;
                results1 = query.sendQuery(req);
                results1.next();
                if(results1.getInt(1)==0) {
                    req = "INSERT INTO expression_rating VALUES (NULL,"+idHash+",0,0,0,0)";
                    query.sendUpdate(req);
                }

                switch(eval) {
                    case "against":
                        req = "UPDATE expression_rating SET nb_against = nb_against + 1 WHERE fk_id_expression = "+idHash;
                        break;
                    case "favorable":
                        req = "UPDATE expression_rating SET nb_favorable = nb_favorable + 1 WHERE fk_id_expression = "+idHash;
                        break;
                    case "neutral":
                        req = "UPDATE expression_rating SET nb_neutral = nb_neutral + 1 WHERE fk_id_expression = "+idHash;
                        break;
                    case "cantSay":
                        req = "UPDATE expression_rating SET nb_cantSay = nb_cantSay + 1 WHERE fk_id_expression = "+idHash;
                        break;
                }
                query.sendUpdate(req);
            }
        }
    }
}
