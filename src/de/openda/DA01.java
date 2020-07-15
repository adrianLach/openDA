package de.openda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DA01 {
	
	protected static ArrayList<Connection> connections = new ArrayList<>();
	protected static int bindedConnection = -1;
	
	protected static Connection getBindedConnection() {
		return connections.get(bindedConnection);
	}
	
	public static int daGenConnection(String server, String schema, String user, String password) {
		Connection connect = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + server + "/" + schema + "?serverTimezone=UTC", user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		connections.add(connect);
		return connections.indexOf(connect);
	}
	
	public static void daDeleteConnection(int index) {
		Connection connect = connections.get(index);
		if(connect == null)
			return;
		try {
			connect.close();
			connections.remove(connect);
			daBindConnection(-1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set to -1 if no Connection should be binded
	 * @param connection
	 */
	public static void daBindConnection(int index) {
		bindedConnection = index;
	}
	
	protected static ArrayList<ResultSet> resultSets = new ArrayList<>();
	protected static int bindedResultSet = -1;

	protected static ResultSet getBindedResultSet() {
		return resultSets.get(bindedResultSet);
	}
	
	public static int daGenResultSet() {
		ResultSet resultSet = null;
		resultSets.add(resultSet);
		return resultSets.indexOf(resultSet);
	}
	
	public static void daDeleteResultSet(int index) {
		ResultSet result = resultSets.get(index);
		if(result == null) {
			return;
		}
		try {
			result.close();
			resultSets.remove(result);
			daBindResultSet(-1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set to -1 if no ResultSet should be binded
	 * @param connection
	 */
	public static void daBindResultSet(int index) {
		bindedResultSet = index;
	}
	
	public static ResultSet daGetResultSet(int index) {
		return resultSets.get(index);
	}

	public static void daExecute(String executeQuery) {
		Connection connect = getBindedConnection();
		if(connect == null)
			return;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connect.createStatement();
			resultSet = statement.executeQuery(executeQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		resultSets.set(bindedResultSet, resultSet);
	}

	public static void initialize() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
