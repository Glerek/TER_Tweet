package Example;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Shows up to 100 of the first retweets of a given tweet.

 */
public  class ChangeDate {


	public static void main(String[] args) {
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

			String req = "SELECT id, Date FROM tweets WHERE 1";

			ResultSet results = my_st.executeQuery(req);

			while (results.next()) {
				System.out.println(results.getString(1) + " : " + results.getString(2));
				String ds = results.getString(2);
				//		ds = ds.replaceAll("CET ", "");
				DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
				//		System.out.println("plop "+df.format(d1));

				Date d = df.parse(ds);
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				String std = df1.format(d);

				String rup = "UPDATE tweets SET date_time='"+std+"' WHERE id='"+results.getString(1)+"';";

				Statement my_st1 = connection.createStatement();
				my_st1.executeUpdate(rup);
			}


		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			System.exit(1);
		}

	}
}