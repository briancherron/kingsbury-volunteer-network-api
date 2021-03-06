package edu.kingsbury.task_tracker.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.realm.JDBCRealm;

import edu.kingsbury.task_tracker.PostgresDao;
import edu.kingsbury.task_tracker.category.Category;

/**
 * Data access object for {@link User}.
 * 
 * <p>
 * This object connects to the PostgreSQL database.
 * 
 * @author brian
 */
public class UserPostgresDao extends PostgresDao implements UserDao {
	
	/**
	 * The standard user role id.
	 */
	//FIXME: don't hard-code this...user types should be determined at runtime
	private static final long STANDARD_USER_ROLE_ID = 2;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<User>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, email, phone, first_name, last_name, facebook, recognition_opt_in from task_tracker.user where (deleted is null or deleted = false) and email <> 'kingsburyvolunteernetwork@gmail.com' "
				+ " order by last_name, first_name asc ");
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getLong("id"));
				user.setEmail(resultSet.getString("email"));
				user.setPhone(resultSet.getString("phone"));
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setFacebook(resultSet.getString("facebook"));
				user.setRecognitionOptIn(resultSet.getBoolean("recognition_opt_in"));
				
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		for (User user : users) {
			user.setCategories(this.findCategories(user.getId()));
		}
			
		return users;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User find(long id) {
		User user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, email, phone, first_name, last_name, facebook, recognition_opt_in, last_login, invitation_key, role_id, authorized_to_change_introduction, password_reset_key from task_tracker.user where id = ? and (deleted is null or deleted = false)");
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getLong("id"));
				user.setEmail(resultSet.getString("email"));
				user.setPhone(resultSet.getString("phone"));
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setFacebook(resultSet.getString("facebook"));
				user.setRecognitionOptIn(resultSet.getBoolean("recognition_opt_in"));
				user.setLastLogin(resultSet.getDate("last_login"));
				user.setInvitationKey((UUID) resultSet.getObject("invitation_key"));
				user.setRoleId(resultSet.getLong("role_id"));
				user.setAuthorizedToChangeIntroduction(resultSet.getBoolean("authorized_to_change_introduction"));
				user.setPasswordResetKey(resultSet.getInt("password_reset_key"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		if (user != null) {
			user.setCategories(this.findCategories(user.getId()));
		}
			
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User find(UUID invitationKey) {
		User user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, email, phone, first_name, last_name, facebook, recognition_opt_in, last_login, invitation_key, role_id, authorized_to_change_introduction, password_reset_key from task_tracker.user where invitation_key = ? and (deleted is null or deleted = false)");
			preparedStatement.setObject(1, invitationKey);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getLong("id"));
				user.setEmail(resultSet.getString("email"));
				user.setPhone(resultSet.getString("phone"));
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setFacebook(resultSet.getString("facebook"));
				user.setRecognitionOptIn(resultSet.getBoolean("recognition_opt_in"));
				user.setLastLogin(resultSet.getDate("last_login"));
				user.setInvitationKey((UUID) resultSet.getObject("invitation_key"));
				user.setRoleId(resultSet.getLong("role_id"));
				user.setAuthorizedToChangeIntroduction(resultSet.getBoolean("authorized_to_change_introduction"));
				user.setPasswordResetKey(resultSet.getInt("password_reset_key"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		if (user != null) {
			user.setCategories(this.findCategories(user.getId()));
		}
			
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User find(String email) {
		User user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, email, phone, first_name, last_name, facebook, recognition_opt_in, last_login, invitation_key, role_id, authorized_to_change_introduction, password_reset_key from task_tracker.user where email = ? and (deleted is null or deleted = false)");
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getLong("id"));
				user.setEmail(resultSet.getString("email"));
				user.setPhone(resultSet.getString("phone"));
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setFacebook(resultSet.getString("facebook"));
				user.setRecognitionOptIn(resultSet.getBoolean("recognition_opt_in"));
				user.setLastLogin(resultSet.getDate("last_login"));
				user.setInvitationKey((UUID) resultSet.getObject("invitation_key"));
				user.setRoleId(resultSet.getLong("role_id"));
				user.setAuthorizedToChangeIntroduction(resultSet.getBoolean("authorized_to_change_introduction"));
				user.setPasswordResetKey(resultSet.getInt("password_reset_key"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		if (user != null) {
			user.setCategories(this.findCategories(user.getId()));
		}
			
		return user;
	}
	
	/**
	 * {@inheritDoc
	 */
	@Override
	public void updateLastLogin(String email) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement("update task_tracker.user set last_login = current_timestamp where email = ? ");
			preparedStatement.setString(1, email);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User create(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		UUID uuid = UUID.randomUUID();
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"insert into task_tracker.user(email, password, role_id, phone, first_name, last_name, facebook, recognition_opt_in, invitation_key) values(?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, JDBCRealm.Digest(user.getPassword(), "MD5", "UTF-8"));
			preparedStatement.setLong(3, STANDARD_USER_ROLE_ID);
			preparedStatement.setString(4, user.getPhone());
			preparedStatement.setString(5, user.getFirstName());
			preparedStatement.setString(6, user.getLastName());
			preparedStatement.setString(7, user.getFacebook());
			preparedStatement.setBoolean(8, user.isRecognitionOptIn());
			preparedStatement.setObject(9, uuid);
			
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (resultSet.next()) {
				user.setId(resultSet.getLong(1));
				user.setInvitationKey(uuid);
			}
			user.setPassword(null); // don't send the password back
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User update(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"update task_tracker.user set phone = ?, first_name = ?, last_name = ?, facebook = ?, recognition_opt_in = ?, email = ?, password_reset_key = ? where id = ?");
			preparedStatement.setString(1, user.getPhone());
			preparedStatement.setString(2, user.getFirstName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.setString(4, user.getFacebook());
			preparedStatement.setBoolean(5, user.isRecognitionOptIn());
			preparedStatement.setString(6, user.getEmail());
			if (user.getPasswordResetKey() > 99999) {
				preparedStatement.setInt(7, user.getPasswordResetKey());
			} else {
				preparedStatement.setNull(7, Types.INTEGER);
			}
			preparedStatement.setLong(8, user.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean changePassword(long id, String newPassword, String currentPassword) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean changed = false;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				currentPassword == null
					? "update task_tracker.user set password= ? where id = ? and password is null"
					: "update task_tracker.user set password= ? where id = ? and password = ?");
			preparedStatement.setString(1, JDBCRealm.Digest(newPassword, "MD5", "UTF-8"));
			preparedStatement.setLong(2, id);
			if (currentPassword != null) {
				preparedStatement.setString(3, JDBCRealm.Digest(currentPassword, "MD5", "UTF-8"));
			}
			changed = preparedStatement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return changed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean resetPassword(long id, String password, int passwordResetKey) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean changed = false;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"update task_tracker.user set password= ?, password_reset_key = null where id = ? and password_reset_key = ? and password_reset_key is not null");
			preparedStatement.setString(1, JDBCRealm.Digest(password, "MD5", "UTF-8"));
			preparedStatement.setLong(2, id);
			preparedStatement.setInt(3, passwordResetKey);
			changed = preparedStatement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return changed;
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
				"delete from task_tracker.user where id = ?");
			preparedStatement.setLong(1, id);
			deleted = preparedStatement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return deleted;
	}
	
	/**
	 * Deletes categories from a user.
	 * 
	 * @param userId the user id
	 */
	@Override
	public void removeCategories(long userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"delete from task_tracker.user_category where user_id = ?");
			preparedStatement.setLong(1, userId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * Finds all categories for a user.
	 * 
	 * @param userId the user id
	 * @return the user's categories
	 */
	private List<Category> findCategories(long userId) {
		List<Category> categories = new ArrayList<Category>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, name from task_tracker.category c join task_tracker.user_category uc on c.id = uc.category_id where user_id = ?");
			preparedStatement.setLong(1, userId);
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
	 * Adds categories to a user.
	 * 
	 * @param userId the user id
	 * @param categoryies the categories
	 */
	@Override
	public void addCategories(long userId, List<Category> categories) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		for (Category category : categories) {
			try {
				connection = this.getDataSource().getConnection();
				preparedStatement = connection.prepareStatement(
					"insert into task_tracker.user_category(category_id, user_id) values(?, ?)");
				preparedStatement.setLong(1, category.getId());
				preparedStatement.setLong(2, userId);
				
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.close(connection, preparedStatement, resultSet);
			}
		}
	}
}
