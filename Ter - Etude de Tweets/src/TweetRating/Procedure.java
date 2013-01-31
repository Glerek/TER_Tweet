/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TweetRating;

import BDD.Query;
import Interfaces.PanelEvaluation;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kevinsancho
 */
public class Procedure {
    private ArrayList<OneRow> table;
    private ArrayList<ArrayList<OneRow>> saveByCouple;
    private PanelEvaluation panelEvaluation;
    private int index;
    
    public Procedure() throws SQLException
    {
        int min, max, rand;
        table = new ArrayList<OneRow>();
        saveByCouple = new ArrayList<ArrayList<OneRow>>();
        ResultSet results,results2,results3;
        Query query = new Query();
        String queryString, queryString2, queryString3;
        queryString = "select val_string,fk_id_user from (select val_string,fk_id_user, count(val_string) as number from tweets.notes group by val_string,fk_id_user order by rand()) as test where test.number>100 order by rand() limit 1;";
        results = query.sendQuery(queryString);
        panelEvaluation = new PanelEvaluation();
        panelEvaluation.setVisible(false);
        while(results.next()) 
        {
            ArrayList<OneRow> oneCouple = new ArrayList<OneRow>();
            queryString2 = "select fk_id_tweet,val_string from tweets.notes where val_string = '" + results.getString(1) + "' and fk_id_user = '" + results.getString(2) +"' order by rand() limit 10;";
            results2 = query.sendQuery(queryString2);
            while(results2.next()) 
            {
                queryString3 = "select text from tweets.tweets where id_tweet = '" + results2.getString(1) + "'";
                results3 = query.sendQuery(queryString3);
                while(results3.next())
                {
                    min = 0;
                    max = table.size();
                    rand = min + (int)(Math.random() * ((max - min)));
                    OneRow oneRow = new OneRow(results3.getString(1), results2.getString(2),results.getString(2),results2.getString(1));
                    table.add(rand, oneRow);
                    oneCouple.add(oneRow);
                }   
                
            }
            saveByCouple.add(oneCouple);
        }
        index = 0;
        panelEvaluation.jLabel1.setText("Subject : " + table.get(index).getSubject());
        panelEvaluation.tweetContent.setText(table.get(index).getText());
        panelEvaluation.setVisible(true);
        panelEvaluation.setProcedure(this);
    }
    
    public void tweetSuivant(String evaluation, String comment, String rater) throws SQLException
    {
        table.get(index).setEvaluation(evaluation);
        table.get(index).setComment(comment);
        table.get(index).setRater(rater);
        index=index+1;
        if(index >= table.size())
        {
            panelEvaluation.dispose();
            SaveInBdd generalization = new SaveInBdd(saveByCouple);
            
        }
        else
        {
            panelEvaluation.jLabel1.setText("Subject : " + table.get(index).getSubject());
            panelEvaluation.tweetContent.setText(table.get(index).getText());
            panelEvaluation.Comment.setText("...");
        }
    }
    
}
