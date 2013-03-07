/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import BDD.Query;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
            result.add(txt.substring(posOc, posNextSpace).replaceAll("[']", ""));
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
        Iterator it;
        ArrayList<String> listeTweet;
        
        req = "SELECT * FROM evaluation_temp WHERE hashtag_analysed = 0";
        results = query.sendQuery(req);
        
        while(results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            System.out.println(id_tweet);
            
            req = "UPDATE evaluation_temp SET hashtag_analysed = 1 WHERE id_evaluation_temp = "+results.getInt(1);
            query.sendUpdate(req);
            query.close();
            
            req = "SELECT text FROM tweets WHERE id_tweet = "+id_tweet;
            results1 = query.sendQuery(req);
            results1.next();
            
            listeTweet = returnHashtags(results1.getString(1));
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
                query.close();
            }
        }
    }
    
    public void updateHashtagRating() throws SQLException {
        String req, nomHashtag, subject;
        ResultSet results, results1;
        Query query = new Query();
        Iterator it;
        int idHash,nb;
        
        req = "SELECT * FROM evaluation_temp WHERE hashtag_rating_analysed = 0";                        
        results = query.sendQuery(req);

        while (results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            String eval = results.getString(3);
            System.out.println(id_tweet);
            
            req = "UPDATE evaluation_temp SET hashtag_rating_analysed = 1 WHERE id_evaluation_temp = "+results.getInt(1);
            query.sendUpdate(req);
            query.close();

            req = "SELECT text, val_string FROM tweets, notes WHERE id_tweet = "+id_tweet+" AND id_tweet = fk_id_tweet";
            results1 = query.sendQuery(req);
            results1.next();
            subject = results1.getString(2);
            ArrayList<String> listeHash = returnHashtags(results1.getString(1));
            it = listeHash.iterator();
            query.close();

            while(it.hasNext()) {
                nomHashtag = (String) it.next();
                //On recupere l'id
                req = "SELECT id_hashtag FROM hashtag WHERE hashtag = '"+nomHashtag+"'";
                results1 = query.sendQuery(req);
                results1.next();
                idHash = results1.getInt(1);
                query.close();

                //On verifie que le hashtag est deja present dans la base
                req = "SELECT COUNT(*) FROM hashtag_rating WHERE fk_id_hashtag = "+idHash+" AND subject = '"+subject+"'";
                results1 = query.sendQuery(req);
                results1.next();
                nb = results1.getInt(1);
                query.close();
                if(nb==0) {
                    req = "INSERT INTO hashtag_rating VALUES (NULL,"+idHash+",'"+subject+"',0,0,0,0)";
                    query.sendUpdate(req);
                    query.close();
                }

                switch(eval) {
                    case "against":
                        req = "UPDATE hashtag_rating SET nb_against = nb_against + 1 WHERE fk_id_hashtag = "+idHash+" AND subject = '"+subject+"'";
                        break;
                    case "favorable":
                        req = "UPDATE hashtag_rating SET nb_favorable = nb_favorable + 1 WHERE fk_id_hashtag = "+idHash+" AND subject = '"+subject+"'";
                        break;
                    case "neutral":
                        req = "UPDATE hashtag_rating SET nb_neutral = nb_neutral + 1 WHERE fk_id_hashtag = "+idHash+" AND subject = '"+subject+"'";
                        break;
                    case "cantSay":
                        req = "UPDATE hashtag_rating SET nb_cantSay = nb_cantSay + 1 WHERE fk_id_hashtag = "+idHash+" AND subject = '"+subject+"'";
                        break;
                }
                query.sendUpdate(req);
                query.close();
            }
        }
    }
    
    public void updateExpressionBase() throws SQLException, IOException, TreeTaggerException {
        String req;
        ResultSet results, results1;
        Iterator it;
        Query query = new Query();
        int nb;
        
        req = "SELECT * FROM evaluation_temp WHERE expression_analysed = 0";                        
        results = query.sendQuery(req);

        while (results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            System.out.println(id_tweet);
            
            req = "UPDATE evaluation_temp SET expression_analysed = 1 WHERE id_evaluation_temp = "+results.getInt(1);
            query.sendUpdate(req);
            query.close();

            req = "SELECT text FROM tweets WHERE id_tweet = "+id_tweet;
            results1 = query.sendQuery(req);
            results1.next();
            TT4J tt = new TT4J(cleanString(results1.getString(1)));
            ArrayList<String> listLemma = tt.getLemma();
            it = listLemma.iterator();
            query.close();

            while(it.hasNext()) {
                String lemma = (String) it.next();
                //On ajout l'expression dans la base s'il n'existe pas
                req = "SELECT COUNT(*) FROM expression WHERE expression = '"+lemma+"'";
                results1 = query.sendQuery(req);
                results1.next();
                nb = results1.getInt(1);
                query.close();
                if(nb==0) {
                    req = "INSERT INTO expression VALUES (NULL,'"+lemma+"',0)";
                    query.sendUpdate(req);
                    query.close();
                }
                req = "UPDATE expression SET nb_occurence = nb_occurence +1 WHERE expression = '"+lemma+"'";
                query.sendUpdate(req);
                query.close();
            }
        }
    }
    
    public void updateExpressionRating() throws SQLException, IOException, TreeTaggerException {
        String req, nomExpression;
        ResultSet results, results1;
        Iterator it;
        int idHash,nb;
        Query query = new Query();
        
        req = "SELECT * FROM evaluation_temp WHERE expression_rating_analysed = 0";
                        
        results = query.sendQuery(req);

        while (results.next()) {
            BigDecimal id_tweet = results.getBigDecimal(2);
            String eval = results.getString(3);
            System.out.println(id_tweet+" / "+eval);
            
            req = "UPDATE evaluation_temp SET expression_rating_analysed = 1 WHERE id_evaluation_temp = "+results.getInt(1);
            query.sendUpdate(req);
            query.close();

            req = "SELECT text FROM tweets WHERE id_tweet = "+id_tweet;
            results1 = query.sendQuery(req);
            results1.next();
            TT4J tt = new TT4J(cleanString(results1.getString(1)));
            ArrayList<String> listLemma = tt.getLemma();
            it = listLemma.iterator();
            query.close();

            while(it.hasNext()) {
                nomExpression = (String) it.next();
                //On recupere l'id
                req = "SELECT id_expression FROM expression WHERE expression = '"+nomExpression+"'";
                results1 = query.sendQuery(req);
                results1.next();
                idHash = results1.getInt(1);
                query.close();

                //On verifie que l'expression est deja presente dans la base
                req = "SELECT COUNT(*) FROM expression_rating WHERE fk_id_expression = "+idHash;
                results1 = query.sendQuery(req);
                results1.next();
                nb=results1.getInt(1);
                query.close();
                if(nb==0) {
                    req = "INSERT INTO expression_rating VALUES (NULL,"+idHash+",0,0,0,0)";
                    query.sendUpdate(req);
                    query.close();
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
                query.close();
            }
        }
    }
    
    public void evaluateTweets(String tweetText, String idTweet) throws SQLException, IOException, TreeTaggerException {
        Iterator it;
        String hashTag, req, nomExpression,subject;
        Query query = new Query();
        ResultSet results;
        ArrayList<String> listeHashtags = returnHashtags(tweetText);
        int total, scoreHash = 0, scoreExpr = 0,nb;
        boolean update = false;
        TT4J tt;
        ArrayList<String> listLemma;
        ArrayList<String> listCand = new ArrayList<String>();
        it = listeHashtags.iterator();
        
        //On construit un tableau avec les noms des candidats, il ne faut pas les prendre en compte dans la routine
        listCand.add("FH2012");
        listCand.add("NS2012");
        listCand.add("Joly");
        listCand.add("MLP2012");
        listCand.add("Mélenchon");
        listCand.add("Montebourg");
        listCand.add("Bayrou");
        
        //On recupere le sujet
        req = "SELECT val_string, COUNT(*) FROM notes WHERE fk_id_tweet = "+idTweet;
        results = query.sendQuery(req);
        results.next();
        //Certains tweets n'ont pas de sujets
        if(results.getInt(2) != 0) {
        subject = results.getString(1);
        query.close();
        
        //On evalue en fonction des hashtags
        while(it.hasNext()) {
            hashTag = (String) it.next();
            //On teste ici si le hashtag existe deja dans la base, si non on ne peut evaluer le hash
            req = "SELECT COUNT(*) FROM hashtag, hashtag_rating WHERE id_hashtag = fk_id_hashtag AND hashtag LIKE '"+hashTag+"' AND subject LIKE '"+subject+"'";
            results = query.sendQuery(req);
            results.next();
            nb=results.getInt(1);
            query.close();
            if(nb!=0 && !listCand.contains(hashTag)) {
                System.out.println(hashTag);
                req = "SELECT * FROM hashtag, hashtag_rating WHERE id_hashtag = fk_id_hashtag AND hashtag LIKE '"+hashTag+"' AND subject LIKE '"+subject+"'";
                results = query.sendQuery(req);
                results.next();
                // 70% pour ou contre on colore sinon on vire
                total = results.getInt("nb_against") + results.getInt("nb_favorable") + results.getInt("nb_neutral") + results.getInt("nb_cantSay");
                if(results.getLong("nb_favorable")>((70*total)/100)) {
                    // Coloré favorable
                    //eval = true;
                    scoreHash += 1;
                }
                else if (results.getLong("nb_against")>((70*total)/100)) {
                    // Coloré against
                    //eval = true;
                    scoreHash -= 1;
                }
                query.close();
            }
        }
        
        //On evalue en fonctions des expressions
        tt = new TT4J(cleanString(tweetText));
        listLemma = tt.getLemma();
        it = listLemma.iterator();
        
        while(it.hasNext()) {
            nomExpression = (String) it.next();
            //System.out.println(nomExpression);
            //On teste ici si l'expression existe deja dans la base, si non on ne peut l'evaluer
            req = "SELECT COUNT(*) FROM expression WHERE expression = '"+nomExpression+"'";
            results = query.sendQuery(req);
            results.next();
            nb = results.getInt(1);
            query.close();
            if(nb!=0) {
                req = "SELECT * FROM expression, expression_rating WHERE id_expression = fk_id_expression AND expression LIKE '"+nomExpression+"'";
                results = query.sendQuery(req);
                results.next();
                // 70% pour ou contre on colore sinon on vire
                total = results.getInt("nb_against") + results.getInt("nb_favorable") + results.getInt("nb_neutral") + results.getInt("nb_cantSay");
                if(results.getLong("nb_favorable")>((70*total)/100)) {
                    // Coloré favorable
                    //eval = true;
                    scoreExpr += 1;
                }
                else if (results.getLong("nb_against")>((70*total)/100)) {
                    // Coloré against
                    //eval = true;
                    scoreExpr -= 1;
                }
                query.close();
            }
        }
        //System.out.println("Tweet : "+tweetText+" / Score Hash : "+scoreHash+" / Score Expr : "+scoreExpr);
        if(scoreExpr < 0 && scoreHash < 0) {
            req = "INSERT INTO evaluation_temp VALUES (NULL,"+idTweet+",'against','"+subject+"',0,0,0,0)";
            query.sendUpdate(req);
            query.close();
            System.out.println(req);
            update = true;
        }
        else if(scoreExpr > 0 && scoreHash > 0) {
            req = "INSERT INTO evaluation_temp VALUES (NULL,"+idTweet+",'favorable','"+subject+"',0,0,0,0)";
            query.sendUpdate(req);
            query.close();
            System.out.println(req);
            update = true;
        }
        
        if(update) {
            //Si on a ajouté quelque chose dans la table evaluation alors on met a jour les autres tables
            updateHashtagBase();
            updateHashtagRating();
            updateExpressionBase();
            updateExpressionRating();
            
            req = "UPDATE tweets SET analysed = 1 WHERE id_tweet = "+idTweet;
            query.sendUpdate(req);
            query.close();
        }
        }
    }
}   
