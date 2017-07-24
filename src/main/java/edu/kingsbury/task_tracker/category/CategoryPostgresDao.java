package edu.kingsbury.task_tracker.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kingsbury.task_tracker.PostgresDao;

/**
 * Data access object for {@link Category}.
 * 
 * <p>
 * This object connects to the PostgreSQL database.
 * 
 * @author brian
 */
public class CategoryPostgresDao extends PostgresDao implements CategoryDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Category> findAll() {
		List<Category> categories = new ArrayList<Category>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, name from task_tracker.category order by name asc ");
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Category category = new Category();
				category.setId(resultSet.getLong("id"));
				category.setName(resultSet.getString("name"));
				
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
			
		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category find(long id) {
		Category category = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, name from task_tracker.category where id = ?");
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				category = new Category();
				category.setId(resultSet.getLong("id"));
				category.setName(resultSet.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category create(Category category) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"insert into task_tracker.category(name) values(?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, category.getName());
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (resultSet.next()) {
				category.setId(resultSet.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category update(Category category) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"update task_tracker.category set name = ? where id = ?");
			preparedStatement.setString(1, category.getName());
			preparedStatement.setLong(2, category.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(long id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean deleted = false;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"delete from task_tracker.category where id = ?");
			preparedStatement.setLong(1, id);
			deleted = preparedStatement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return deleted;
	}
}
