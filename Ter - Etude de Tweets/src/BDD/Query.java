/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDD;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author kevin-sancho
 */
public class Query {
    private String connectionString;
    private String user;
    private String password;
    
    public Query(String requete)
    {
        /*XmlAction xmlAction = new XmlAction();
        xmlAction.parseXmlFile();
        xmlAction.parseDocument();*/
        
        this.connectionString = "jdbc:mysql://localhost:3306/tweets";
        this.user = "root";
        this.password = "";
    }
    
    public ResultSet sendQuery(String queryString)
    {
        ResultSet results = null;
        try {
                        Statement my_st = null;

			java.sql.Connection connection = DriverManager.getConnection(connectionString,user,password);

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
    
}
