/*
		Name: Nicholas Donofrio
		Course: CNT 4714 Spring 2024
		Assignment title: Project 3 – A Two-tier Client-Server Application
		Date: March 10, 2024
		Class: CNT 4714
		*/

// IMPORTS_______________________
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.mysql.cj.jdbc.MysqlDataSource;
//_________________________________________________

// Public Class MAIN SQL APP______________________________________________________________________
public class SQLClientApp extends JFrame
{
	private JButton connectBtn, clearQuery, executeBtn, clearWindow, disconnectBtn;
	private ResultSetTable tableModel;
	private JLabel queryLabel, userLabel, passwordLabel, statusLabel, windowLabel, dbInfoLabel, jdbcLabel, dbPropertiesLabel;
	private JTextArea queryArea;
	private JTextField userField;
	private JLabel blankLabel;
	private JPasswordField passwordField;
	private JComboBox<String> propertiesCombo, urlCombo, dbPropertiesCombo;
	private Connection connection;
	private TableModel empty;
	private JTable resultTable;

	// GUI Constructor Class
	public SQLClientApp()
	{
		setName("Project 3 Spring 2024");
		// Set the title of the window
		setTitle("SQL Client Application-(NJD-CNT4714 - Spring 2024 - Project 3)");
		setSize(1000, 580);
		blankLabel = new JLabel();

		// Initialize the drop-down menus
		String[] PropertiesItems = {"root.properties", "client1.properties", "client2.properties", "theaccountant.properties"};
		String[] dbPropertiesItems = {"project3.properties", "bikedb.properties", "operationslog.properties"};


		// Password Text Area
		queryArea = new JTextArea("");
		queryArea.setEnabled(false);
		userField = new JTextField("");
		passwordField = new JPasswordField();

		// Connect Button
		connectBtn = new JButton("Connect to Database");
		connectBtn.setFont(new Font("Arial", Font.BOLD, 12));
		connectBtn.setForeground(Color.yellow);
		connectBtn.setBackground(Color.blue);

		// Clear Query Button
		clearQuery = new JButton("Clear SQL Command");
		clearQuery.setFont(new Font("Arial", Font.BOLD, 12));
		clearQuery.setForeground(Color.red);
		clearQuery.setBackground(Color.white);
		clearQuery.setEnabled(false);

		// Execute Button
		executeBtn = new JButton("Execute SQL Command");
		executeBtn.setFont(new Font("Arial", Font.BOLD, 12));
		executeBtn.setForeground(Color.black);
		executeBtn.setBackground(Color.green);
		executeBtn.setEnabled(false);

		// Clear Result Button
		clearWindow = new JButton("Clear Result Window");
		clearWindow.setFont(new Font("Arial", Font.BOLD, 12));
		clearWindow.setForeground(Color.black);
		clearWindow.setBackground(Color.yellow);
		clearWindow.setEnabled(false);

		// Disconnect Button
		disconnectBtn = new JButton("Disconnect");
		disconnectBtn.setFont(new Font("Arial", Font.BOLD, 12));
		disconnectBtn.setForeground(Color.black);
		disconnectBtn.setBackground(Color.red);
		disconnectBtn.setEnabled(false);

		// Enter Command Box
		queryLabel = new JLabel("Enter an SQL Command");
		queryLabel.setFont(new Font("Arial", Font.BOLD, 12));
		queryLabel.setForeground(Color.blue);

		// Connection Details Label
		dbInfoLabel = new JLabel("Connections Details");
		dbInfoLabel.setFont(new Font("Arial", Font.BOLD, 12));

		jdbcLabel = new JLabel("User Properties");
		jdbcLabel.setOpaque(true);
		jdbcLabel.setForeground(Color.black);
		jdbcLabel.setBackground(Color.LIGHT_GRAY);

		// DB Properties label
		dbPropertiesLabel = new JLabel("DB Properties");
		dbPropertiesLabel.setOpaque(true);
		dbPropertiesLabel.setForeground(Color.black);
		dbPropertiesLabel.setBackground(Color.LIGHT_GRAY);

		// User Properties Combo Box
		propertiesCombo = new JComboBox<>();
		for (String item : PropertiesItems) {
			propertiesCombo.addItem(item);
		}

		// Username label
		userLabel = new JLabel("Username");
		userLabel.setForeground(Color.black);
		userLabel.setBackground(Color.LIGHT_GRAY);
		userLabel.setOpaque(true);

		// Password Label + Combo
		urlCombo = new JComboBox<>();
		passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.black);
		passwordLabel.setBackground(Color.LIGHT_GRAY);
		passwordLabel.setOpaque(true);

		// DB Properties Combo Box
		dbPropertiesCombo = new JComboBox<>();
		for (String item : dbPropertiesItems) {
			dbPropertiesCombo.addItem(item);
		}

		// Status Label
		statusLabel = new JLabel("No Connection Now");
		statusLabel.setForeground(Color.red);
		statusLabel.setBackground(Color.BLACK);
		statusLabel.setOpaque(true);

		// Execution Window Label
		windowLabel = new JLabel("SQL Execution Result Window");
		windowLabel.setFont(new Font("Arial", Font.BOLD, 12));
		windowLabel.setForeground(Color.blue);

		// Execution table
		resultTable = new JTable(tableModel);
		JScrollPane square = new JScrollPane(resultTable);
		square.setOpaque(true);
		square.setBackground(Color.WHITE);

		// Button pos
		connectBtn.setBounds(20, 187, 165, 25);
		clearQuery.setBounds(470, 150, 165, 25);
		executeBtn.setBounds(680, 150, 170, 25);
		clearWindow.setBounds(45, 500, 165, 25);
		disconnectBtn.setBounds(20, 150, 165, 25);

		// Label pos
		userLabel.setBounds(10, 78, 125, 25);
		passwordLabel.setBounds(10, 107, 125, 24);
		jdbcLabel.setBounds(10, 21, 125, 25);
		dbPropertiesLabel.setBounds(10, 50, 125, 25);
		statusLabel.setBounds(200, 187, 690, 25);
		windowLabel.setBounds(45, 231, 220, 25);
		square.setBounds(45, 254, 841, 220);
		queryLabel.setBounds(450, 0, 215, 25);
		queryArea.setBounds(450, 21, 420, 120);
		propertiesCombo.setBounds(135, 21, 290, 25);
		dbPropertiesCombo.setBounds(135, 50, 290, 25);
		urlCombo.setBounds(135, 49, 290, 25);
		userField.setBounds(135, 78, 290, 25);
		passwordField.setBounds(135, 106, 290, 25);

		// Add all
		add(connectBtn);
		add(disconnectBtn);
		add(clearQuery);
		add(executeBtn);
		add(queryLabel);
		add(square);
		add(queryArea);
		add(dbInfoLabel);
		add(jdbcLabel);
		add(propertiesCombo);
		add(dbPropertiesLabel);
		add(dbPropertiesCombo);
		add(userLabel);
		add(userField);
		add(passwordLabel);
		add(passwordField);
		add(statusLabel);
		add(windowLabel);
		add(clearWindow);
		add(blankLabel);

		// Disconnect button___________________________________
		disconnectBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				disconnectFromDatabase();
				dbPropertiesCombo.setEnabled(true);
			}
		});

		// Connect button______________________________________
		connectBtn.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent event)
			{
				boolean usernameMatch = false;
				boolean passwordMatch = false;
				try
				{
					// If connected,close connection
					if (connection != null) {
						connection.close();
					}
					statusLabel.setText("No Connection Now");

					Properties properties = new Properties();
					FileInputStream filein = null;
					MysqlDataSource dataSource = null;
					Connection connectionToOpLog = null;

					// Read user properties
					try {
						filein = new FileInputStream((String) propertiesCombo.getSelectedItem());
						properties.load(filein);

						// set the parameters
						dataSource = new MysqlDataSource();
						// get url
						//dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));

						// Match credentials to properties
						if (userField.getText().equals((String) properties.getProperty("MYSQL_DB_USERNAME"))) {
							usernameMatch = true;
							if (passwordField.getText().equals((String) properties.getProperty("MYSQL_DB_PASSWORD"))) {
								passwordMatch = true;
							}
						}

						// If credentials match log the user in.
						if (passwordMatch && usernameMatch)
						{
							dataSource.setUser((String) properties.getProperty("MYSQL_DB_USERNAME"));
							dataSource.setPassword((String) properties.getProperty("MYSQL_DB_PASSWORD"));

							// establish a connection
							connection = dataSource.getConnection();
							// update status label
							statusLabel.setText("Connected to " + (String) properties.getProperty("MYSQL_DB_URL"));
							clearQuery.setEnabled(true);
							executeBtn.setEnabled(true);
							clearWindow.setEnabled(true);
							queryArea.setEnabled(true);
							// Enable the Disconnect button when connected
							disconnectBtn.setEnabled(true);

							userField.setEditable(false);
							passwordField.setEditable(false);
							connectBtn.setEnabled(false);
							propertiesCombo.setEnabled(false);
							dbPropertiesCombo.setEnabled(false);
						} else {
							statusLabel.setText("NOT CONNECTED - User Credentials Do Not Match Properties File");
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					}

					// Read db properties file
					try
					{
						filein = new FileInputStream((String) dbPropertiesCombo.getSelectedItem());
						properties.load(filein);

						// set the database parameters
						dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));

						// establish a connection to the database
						connection = dataSource.getConnection();
						// update status label
						statusLabel.setText("Connected to " + (String) properties.getProperty("MYSQL_DB_URL"));
						clearQuery.setEnabled(true);
						executeBtn.setEnabled(true);
						clearWindow.setEnabled(true);
						queryArea.setEnabled(true);
						// Enable the Disconnect button when connected
						disconnectBtn.setEnabled(true);

						userField.setEditable(false);
						passwordField.setEditable(false);
						connectBtn.setEnabled(false);
						propertiesCombo.setEnabled(false);

					} catch (SQLException e)
					{
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e)
					{
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
//____________________________________________________________________________________________________________

		// Clear Query Button____________________________________________________
		clearQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				queryArea.setText("");
			}
		});
//_________________________________________________________________________________________

		// Clear result button___________________________________________________________________
		clearWindow.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				empty = new DefaultTableModel();
				resultTable.setModel(empty);
			}
		});
//_____________________________________________________________________________________________________

		// Execute Button______________________________________________________________________
		executeBtn.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					String query = queryArea.getText();
					System.out.println("Executing query: " + query);

					if (query.toLowerCase().startsWith("select"))
					{
						System.out.println("Executing SELECT query");
						tableModel = new ResultSetTable(connection, query);
						tableModel.setQuery(query);
					} else
					{
						System.out.println("Executing UPDATE/INSERT/DELETE query");
						tableModel = new ResultSetTable(connection, query);
						tableModel.setUpdate(query);
						empty = new DefaultTableModel();
						resultTable.setModel(empty);
					}

					resultTable.setModel(tableModel);

				} catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException NotFound)
				{
					JOptionPane.showMessageDialog(null, "MySQL driver not found", "Driver not found", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
//______________________________________________________________________________________________________________________________________________________________


	// Disconnect method___________________
	private void disconnectFromDatabase()
	{
		try
		{
			if (connection != null)
			{
				connection.close();
			}
			connection = null;
			statusLabel.setText("No Connection Now");
			clearQuery.setEnabled(false);
			executeBtn.setEnabled(false);
			clearWindow.setEnabled(false);
			queryArea.setEnabled(false);
			userField.setEditable(true);
			passwordField.setEditable(true);
			connectBtn.setEnabled(true);
			propertiesCombo.setEnabled(true);
			disconnectBtn.setEnabled(false);

			// Clear the result table
			empty = new DefaultTableModel();
			resultTable.setModel(empty);
		} catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Main__________________
	public static void main(String[] args)
	{
		SQLClientApp project = new SQLClientApp();
		project.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		project.setVisible(true);
	}
}
