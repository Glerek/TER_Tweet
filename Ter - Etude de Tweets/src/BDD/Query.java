/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDD;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author kevin-sancho
 */
public class Query {
    private String connectionString;
    private String user;
    private String password;
    private java.sql.Connection connection;
    
    public void close() throws SQLException
    {
        this.connection.close();
    }
    
    public Query()
    {
        /*XmlAction xmlAction = new XmlAction();
        xmlAction.parseXmlFile();
        xmlAction.parseDocument();*/
        
        //this.connectionString = "jdbc:mysql://localhost:3306/tweets";
        this.connectionString = "jdbc:mysql://localhost/tweets";
        this.user = "root";
        this.password = "";
    }
    
    public Query(int indiceTable)
    {
        /*XmlAction xmlAction = new XmlAction();
        xmlAction.parseXmlFile();
        xmlAction.parseDocument();*/
        if(indiceTable == 1)
        {
        this.connectionString = "jdbc:mysql://localhost:3306/auto";
        this.user = "root";
        this.password = "";
        }
        else
        {
            this.connectionString = "jdbc:mysql://localhost:3306/tweets";
             this.user = "root";
             this.password = "";
        }
    }
    
    
    
    public ResultSet sendQuery(String queryString)
    {
        ResultSet results = null;
        try {
                        Statement my_st = null;

			this. connection = DriverManager.getConnection(connectionString,user,password);

			my_st = connection.createStatement();

			results = my_st.executeQuery(queryString);
			return results;

		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			System.exit(1);
		}
        return results;
    }
    
    public void sendUpdate(String queryString)
    {
        try {
                        Statement my_st = null;

			this.connection = DriverManager.getConnection(connectionString,user,password);

			my_st = connection.createStatement();

			my_st.executeUpdate(queryString);

		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			System.exit(1);
		}
    }
    
    private void sauvegarde()
    {
        String sauvegarde = "select val_string,fk_id_user, count(val_string) from tweets.notes group by val_string,fk_id_user order by rand() limit 50";
    }
    
}
