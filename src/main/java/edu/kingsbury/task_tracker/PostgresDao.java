package edu.kingsbury.task_tracker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Base object for PostgreSQL data access.
 * 
 * @author brian
 */
public class PostgresDao {

	/**
	 * The data source.
	 */
	private DataSource dataSource;
	
	/**
	 * Constructor initializes the data source.
	 * 
	 * @throws Exception if an error occurs initializing the data source
	 */
	public PostgresDao() {
		try {
			this.dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/kingsbury");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes a connection, statement, and result set.
	 * 
	 * @param connection the connection
	 * @param statement the statement
	 * @param resultSet the result set
	 * @throws Exception if an error occurs closing the connection, statement, or result set.
	 */
	public void close(Connection connection, Statement statement, ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}
}
