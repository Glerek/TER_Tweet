/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TweetRating;

import BDD.Query;
import ReadXml.XmlIncrement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author kevinsancho
 */
public class SaveInBdd 
{
    private ArrayList<ArrayList<OneRow>> results;
    
    public SaveInBdd(ArrayList<ArrayList<OneRow>> results) throws SQLException
    {
        this.results = results;
        int nbTotal,nbFavorable,nbAgainst,nbCantSay,nbNeutral;
        String evaluation;
        int index = 0;
        for(ArrayList<OneRow> iterationArray : results)
        {
            nbTotal = nbFavorable = nbAgainst = nbCantSay = nbNeutral = 0;
            for(OneRow iterationRow : iterationArray)
            {
                evaluation = iterationRow.getEvaluation();
                if(evaluation.equals("against"))
                {
                    nbAgainst = nbAgainst+1;
                }
                if(evaluation.equals("cantSay"))
                {
                    nbCantSay = nbCantSay+1;
                }
                if(evaluation.equals("neutral"))
                {
                    nbNeutral = nbNeutral+1;
                }
                if(evaluation.equals("favorable"))
                {
                    nbFavorable = nbFavorable+1;
                }
                nbTotal = nbTotal+1;
            }
            analysis(nbTotal,nbAgainst,nbNeutral,nbFavorable,nbCantSay,index);
            index = index +1;
        }
    }
    
    private void analysis(int nbTotal,int nbAgainst,int nbNeutral,int nbFavorable,int nbCantSay,int index) throws SQLException
    {
        Query query = new Query();
        String queryString,queryString2,queryString3;
        for(OneRow rowIteration : results.get(index))
        {
            XmlIncrement xmlIncrement = new XmlIncrement();
            String increment = xmlIncrement.parseXmlFile();
                
            queryString = "insert into tweets.evaluation(fk_id_tweet,rater,evaluation,comment,evaluation_groupe) values('" +rowIteration.getHideIdTweet()+"','"+rowIteration.getRater()+"','"+rowIteration.getEvaluation()+"','"+rowIteration.getComment()+"','"+increment+"')";
            query.sendUpdate(queryString);
            
            int intIncrement = Integer.parseInt(increment);
            intIncrement = intIncrement +1;
            increment = String.valueOf(intIncrement);
        }
        
        
        /*
         * PARTIE POUR L'ENREGISTREMENT DE LA GENERALISATION EN BASE 
         * 
        nbAgainst = nbAgainst/nbTotal *100;
        nbNeutral = nbNeutral/nbTotal *100;
        nbFavorable = nbFavorable/nbTotal *100;
        nbCantSay = nbCantSay/nbTotal *100;
        if(nbCantSay <= 10)
        {
            if(nbNeutral >= 40)
            {
                JOptionPane.showMessageDialog(null, "Tweet from user n°" + results.get(index).get(0).getHideAuthor() + "with subject " + results.get(index).get(0).getSubject() + "may contain interesting information and can't be generalize");
            }
            else
            {
                queryString2 = "select fk_id_tweet from tweets.notes where fk_id_user='" + results.get(index).get(0).getHideAuthor() +"' and val_string='" + results.get(index).get(0).getSubject()+"';";
                ResultSet results2 = query.sendQuery(queryString2);  
                if((nbFavorable >= 90) || (nbFavorable >= 70 && nbAgainst <= 10))
                {
                    while(results2.next())
                    {
                        queryString3 = "insert into tweets.evaluation(fk_id_tweet,rater,evaluation,comment) values('" +results2.getString(1)+"','Automatic','favorable','')";
                    }
                }
                else
                {
                    if((nbAgainst >= 70) || (nbAgainst >= 70 && nbNeutral <= 10))
                    {
                        while(results2.next())
                        {
                            queryString3 = "insert into tweets.evaluation(fk_id_tweet,rater,evaluation,comment) values('" +results2.getString(1)+"','Automatic','against','')";
                        }
                    }
                }
            }
        }
        */
    }
}
