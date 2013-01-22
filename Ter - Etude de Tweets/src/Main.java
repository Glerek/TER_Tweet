
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

		try {

			java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ter","root","");

			my_st = connection.createStatement();

			String req = "SELECT * FROM tweets WHERE fk_nom_user='fhollande' AND date_time>'2012-02-23 00:00:00' AND date_time<'2012-02-25 00:00:00'";

			ResultSet results = my_st.executeQuery(req);
			while (results.next()) {
				System.out.println(results.getString(5));
			}


		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			System.exit(1);
		}

	}
    }
