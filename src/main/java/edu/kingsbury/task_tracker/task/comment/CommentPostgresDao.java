package edu.kingsbury.task_tracker.task.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.kingsbury.task_tracker.PostgresDao;
import edu.kingsbury.task_tracker.user.User;

/**
 * Data access object for {@link Comment}.
 * 
 * <p>
 * This implementation connects to the Postgres database.
 * 
 * @author brian
 */
public class CommentPostgresDao extends PostgresDao implements CommentDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comment add(Comment comment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" insert into task_tracker.comment(task_id, comment, user_added, date_added) values (?, ?, ?, current_timestamp) ", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, comment.getTaskId());
			preparedStatement.setString(2, comment.getText());
			preparedStatement.setLong(3, comment.getUser().getId());
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (resultSet.next()) {
				comment.setId(resultSet.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return this.find(comment.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comment edit(Comment comment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" update task_tracker.comment set comment = ?, date_edited = current_timestamp where id = ? and user_added = ? ");
			preparedStatement.setString(1, comment.getText());
			preparedStatement.setLong(2, comment.getId());
			preparedStatement.setLong(3, comment.getUser().getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return this.find(comment.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comment delete(long commentId, long userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" update task_tracker.comment set deleted = true, date_edited = current_timestamp where id = ? and user_added = ? ");
			preparedStatement.setLong(1, commentId);
			preparedStatement.setLong(2, userId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return this.find(commentId);
	}
	
	/**
	 * Finds a comment.
	 * 
	 * @param id the id
	 * @return the comment
	 */
	@Override
	public Comment find(long id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Comment comment = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" select c.id, c.task_id, c.comment, c.user_added, c.date_added, c.date_edited, c.deleted, u.email, u.first_name, u.last_name "
				+ " from task_tracker.comment c"
				+ " left join task_tracker.user u on c.user_added = u.id "
				+ " where c.id = ? ");
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				comment = new Comment();
				comment.setId(resultSet.getLong("id"));
				comment.setTaskId(resultSet.getLong("task_id"));
				comment.setText(resultSet.getString("comment"));
				comment.setDateAdded(new Date(resultSet.getTimestamp("date_added").getTime()));
				comment.setDateEdited(resultSet.getDate("date_edited"));
				comment.setDeleted(resultSet.getBoolean("deleted"));
				comment.setUser(new User());
				comment.getUser().setId(resultSet.getLong("user_added"));
				comment.getUser().setEmail(resultSet.getString("email"));
				comment.getUser().setFirstName(resultSet.getString("first_name"));
				comment.getUser().setLastName(resultSet.getString("last_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return comment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Comment> findComments(long taskId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Comment> comments = new ArrayList<Comment>();
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				" select c.id, c.task_id, c.comment, c.user_added, c.date_added, c.date_edited, c.deleted, u.email, u.first_name, u.last_name "
				+ " from task_tracker.comment c"
				+ " left join task_tracker.user u on c.user_added = u.id "
				+ " where task_id = ? and (c.deleted is null or c.deleted = false) "
				+ " order by date_added asc ");
			preparedStatement.setLong(1, taskId);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Comment comment = new Comment();
				comment.setId(resultSet.getLong("id"));
				comment.setTaskId(resultSet.getLong("task_id"));
				comment.setText(resultSet.getString("comment"));
				comment.setDateAdded(new Date(resultSet.getTimestamp("date_added").getTime()));
				comment.setDateEdited(resultSet.getDate("date_edited"));
				comment.setDeleted(resultSet.getBoolean("deleted"));
				comment.setUser(new User());
				comment.getUser().setId(resultSet.getLong("user_added"));
				comment.getUser().setEmail(resultSet.getString("email"));
				comment.getUser().setFirstName(resultSet.getString("first_name"));
				comment.getUser().setLastName(resultSet.getString("last_name"));
				
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return comments;
	}
}
