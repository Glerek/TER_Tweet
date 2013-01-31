
import ReadXml.XmlConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    public static void main(String[] args) 
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

	}
    }
