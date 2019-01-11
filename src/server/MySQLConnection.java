package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLConnection {
	final String DATABASE_URL = "jdbc:mysql://localhost/";
	private Connection conn;
	private String schema;

	public MySQLConnection(String schema, String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}

		try {
			this.schema = schema;
			conn = DriverManager.getConnection(DATABASE_URL + schema, userName, password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.exit(0);
		}
	}

	public Object executeArrayQuery(Object msg) {
		ArrayList<String> arr = (ArrayList<String>) msg;
		try {
			//getting the query from arr
			String Query =arr.get(arr.size()-1);
			
			if (Query.startsWith("SELECT")) {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(Query);
				System.out.println("DB: " + Query + " => Executed Successfully");
				return parseResultSet(rs);
				
			} else if (Query.startsWith("INSERT") || Query.startsWith("UPDATE")) {
				int i;
				PreparedStatement ps = conn.prepareStatement(Query);
				for(i=0; i<(arr.size()-1); i++) {
					ps.setString(i+1, arr.get(i));
				}
				ps.executeUpdate();
				System.out.println("DB: " + Query + " => Executed Successfully");
				return null;
			}
		} catch (SQLException sqlException) {
			System.out.println("Couldn't execute query");
			sqlException.getStackTrace();
			return null;
		}
		return null;
	}
	
	public Object executeQuery(Object sQuery) {

		try {
			String Query = (String) sQuery;
			if (Query.startsWith("SELECT")) {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(Query);
				System.out.println("DB: " + Query + " => Executed Successfully");
				return parseResultSet(rs);
			} else if (Query.startsWith("INSERT") || Query.startsWith("UPDATE")) {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(Query);
				System.out.println("DB: " + Query + " => Executed Successfully");
				return null;
			}
		} catch (SQLException sqlException) {
			System.out.println("Couldn't execute query");
			sqlException.getStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Parse database result set into an ArrayList with rows separated by commas
	 * @param rs
	 * @return arr
	 */
	public ArrayList<String> parseResultSet(ResultSet rs) {
		ArrayList<String> arr = new ArrayList<>();
		int i;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				i = 1;
				while (i < rsmd.getColumnCount()) {
					arr.add(rs.getString(i++));
				}
				arr.add("\n");
			}
		} catch (SQLException Exception) {
			System.out.println("ERROR while parsing array!");
		}
		return arr;
	}
}
