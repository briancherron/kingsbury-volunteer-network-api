package edu.kingsbury.task_tracker.introduction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.kingsbury.task_tracker.PostgresDao;

/**
 * Performs data access for the app introduction.
 * 
 * @author brian
 */
public class IntroductionPostgresDao extends PostgresDao implements IntroductionDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String findIntroduction() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String introduction = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" select text "
				+ " from task_tracker.introduction "
				+ " where id = (select max(id) from task_tracker.introduction) ");
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				introduction = resultSet.getString("text");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return introduction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveIntroduction(String introduction) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" insert into task_tracker.introduction(text, date_added) values (?, current_timestamp) ");
			preparedStatement.setString(1, introduction);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}
}
