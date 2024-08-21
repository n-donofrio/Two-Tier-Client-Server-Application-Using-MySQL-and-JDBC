/*
Name: Nicholas Donofrio
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Specialized Accountant Application
Date: March 10, 2024
Class: CNT 4714
*/

import java.io.FileInputStream;
import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;


public class ResultSetTable extends AbstractTableModel{
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numOfRows;

	// Track connection
	private boolean connectedToDb = false;


// Result Table________________________________________________________________________________________
	public ResultSetTable (Connection c, String query) throws SQLException, ClassNotFoundException
	{
		// Statement
		statement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// Update Status
		connectedToDb = true;

	}

	// Method to get class from column_______________________________________
	public Class getColumnClass (int column) throws IllegalStateException
	{
		// Check if database is connected
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Datase");
		}

		// Check java clas of column
		try {
			String className = metaData.getColumnClassName(column + 1);

			// Return Class object
			return Class.forName(className);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}

		return Object.class;
	} // END
	//____________________________________________


	// Method to return num rows_________________________________________
	public int getRowCount() throws IllegalStateException
	{
		// ensure the database is connected
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Datase");
		}

		return numOfRows;
	}// END
	//____________________________________________________________________

	// Method to retreive number columns___________________________________
	public int getColumnCount() throws IllegalStateException
	{
		// ensure the database is connected
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Datase");
		}

		try
		{
			return metaData.getColumnCount();
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		return 0;
	}// END
	//______________________________________________

	 // Method to retrieve column name____________________________________________
	public String getColumnName(int column) throws IllegalStateException
	{
		// Check if database is connected
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Datase");
		}

		// Find column name
		try
		{
			return metaData.getColumnName(column + 1);
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return "";
	}// END
	//____________________________________________

	// Method to retreive value
	public Object getValueAt(int rowIndex, int columnIndex) throws IllegalStateException
	{
		// Check database connection
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Datase");
		}

		// Get value
		try
		{
			resultSet.next();
			resultSet.absolute(rowIndex + 1);
			return resultSet.getObject(columnIndex + 1);
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return ""; // Return empty string
	}// END
	//_________________________________________________________


	// Method for setQuery
	public void setQuery(String query) throws SQLException, IllegalStateException
	{
		// Check db connection
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Database");
		}

		// Make query
		resultSet = statement.executeQuery(query);

		// Meta data for result set
		metaData = resultSet.getMetaData();

		// Determine num of rows
		resultSet.last(); // Change to last row
		numOfRows = resultSet.getRow(); // Get current row

		// Update OPLog
		updateOpLog("num_queries");

		// Model change
		fireTableStructureChanged();
	}// END
	//________________________________________________________

	// Method for set update
	public void setUpdate(String query) throws SQLException, IllegalStateException
	{
		int res;

		// Check if database is connected
		if (!connectedToDb)
		{
			throw new IllegalStateException("Not connected to Database");
		}

		// Execute query
		res = statement.executeUpdate(query);

		// Update num updates
		updateOpLog("num_updates");

		// Model update
		fireTableStructureChanged();
	}// END
	//_________________________________________________________

	// Method to update OP log
	public void updateOpLog(String para)
	{
		// Update operations
		Properties properties = new Properties();
		FileInputStream filein = null;
		MysqlDataSource dataSource = null;
		Connection connectionToOpLog = null;

		// Read file
		try {
			filein = new FileInputStream("oplog.properties");
			properties.load(filein);

			// Parameters
			dataSource = new MysqlDataSource();
			dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));
			dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
			dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

			// Establish connection
			connectionToOpLog = dataSource.getConnection();

			// Statement
			Statement opLogsStatement = connectionToOpLog.createStatement();
			opLogsStatement.executeUpdate("UPDATE operationscount set " + para + " = "  + para +" + 1");

			// Close connection
			connectionToOpLog.close();
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}
	// C

}
