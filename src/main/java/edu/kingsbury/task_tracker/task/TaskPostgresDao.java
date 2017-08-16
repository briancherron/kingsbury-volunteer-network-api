package edu.kingsbury.task_tracker.task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import edu.kingsbury.task_tracker.PostgresDao;
import edu.kingsbury.task_tracker.category.Category;
import edu.kingsbury.task_tracker.partner.Partner;
import edu.kingsbury.task_tracker.user.User;

/**
 * Data access object for {@link Task}.
 * 
 * <p>
 * This object connects to the PostgreSQL database.
 * 
 * @author brian
 */
public class TaskPostgresDao extends PostgresDao implements TaskDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Task> find(Filter filter) {
		List<Task> tasks = new ArrayList<Task>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int index = 0;
		
		StringBuilder query = new StringBuilder("");
		query.append(" select t.id, t.name, t.date, t.description, t.status as status_id, t.date_added, t.user_added, t.date_modified, t.user_modified, ");
		query.append("        s.name as status_name, ");
		query.append("        ua.id as ua_id, ua.email as ua_email, ua.phone as ua_phone, ua.first_name as ua_first_name, ua.last_name as ua_last_name, ua.facebook as ua_facebook, ua.recognition_opt_in as ua_recognition_opt_in, ");
		query.append("        um.id as um_id, um.email as um_email, um.phone as um_phone, um.first_name as um_first_name, um.last_name as um_last_name, um.facebook as um_facebook, um.recognition_opt_in as um_recognition_opt_in ");
		query.append(" from task_tracker.task t");
		query.append("      left join task_tracker.task_status s on t.status = s.id ");
		query.append("      left join task_tracker.user ua on t.user_added = ua.id ");
		query.append("      left join task_tracker.user um on t.user_modified = um.id ");
		query.append(" where (t.deleted is null or t.deleted = false) ");
		if (StringUtils.isNotBlank(filter.getQuery())) {
			query.append("   and lower(t.name) like lower(?) ");
		}
		if (filter.getStatusId() != -1) {
			query.append("   and t.status = ? ");
		}
		if (filter.getUserId() != -1) {
			query.append("   and exists (select 1 from task_tracker.task_user where user_id = ? and task_id = t.id and status_id = 2) ");
		}
		if (filter.getCategoryId() != -1) {
			query.append("   and exists (select 1 from task_tracker.task_category where category_id = ? and task_id = t.id) ");
		}
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(query.toString());
			if (StringUtils.isNotBlank(filter.getQuery())) {
				preparedStatement.setString(++index, "%" + filter.getQuery() + "%");
			}
			if (filter.getStatusId() != -1) {
				preparedStatement.setLong(++index, filter.getStatusId());
			}
			if (filter.getUserId() != -1) {
				preparedStatement.setLong(++index, filter.getUserId());
			}
			if (filter.getCategoryId() != -1) {
				preparedStatement.setLong(++index, filter.getCategoryId());	
			}
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Task task = new Task();
				task.setId(resultSet.getLong("id"));
				task.setName(resultSet.getString("name"));
				task.setDate(resultSet.getDate("date"));
				task.setDescription(resultSet.getString("description"));
				task.setStatus(new TaskStatus());
				task.getStatus().setId(resultSet.getLong("status_id"));
				task.getStatus().setName(resultSet.getString("status_name"));
				task.setDateAdded(resultSet.getDate("date_added"));
				task.setUserAdded(new User());
				task.getUserAdded().setId(resultSet.getLong("ua_id"));
				task.getUserAdded().setEmail(resultSet.getString("ua_email"));
				task.getUserAdded().setPhone(resultSet.getString("ua_phone"));
				task.getUserAdded().setFirstName(resultSet.getString("ua_first_name"));
				task.getUserAdded().setLastName(resultSet.getString("ua_last_name"));
				task.getUserAdded().setFacebook(resultSet.getString("ua_facebook"));
				task.getUserAdded().setRecognitionOptIn(resultSet.getBoolean("ua_recognition_opt_in"));
				task.setDateModified(resultSet.getDate("date_modified"));
				task.setUserModified(new User());
				task.getUserModified().setId(resultSet.getLong("um_id"));
				task.getUserModified().setEmail(resultSet.getString("um_email"));
				task.getUserModified().setPhone(resultSet.getString("um_phone"));
				task.getUserModified().setFirstName(resultSet.getString("um_first_name"));
				task.getUserModified().setLastName(resultSet.getString("um_last_name"));
				task.getUserModified().setFacebook(resultSet.getString("um_facebook"));
				task.getUserModified().setRecognitionOptIn(resultSet.getBoolean("um_recognition_opt_in"));
				
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		for (Task task : tasks) {
			task.setCategories(this.findCategories(task.getId()));
			task.setUsers(this.findUsers(task.getId()));
			task.setPartners(this.findPartners(task.getId()));
		}
			
		return tasks;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TaskStatus> findAllStatuses() {
		List<TaskStatus> statuses = new ArrayList<TaskStatus>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select id, name from task_tracker.task_status");
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				TaskStatus status = new TaskStatus();
				status.setId(resultSet.getLong("id"));
				status.setName(resultSet.getString("name"));
				
				statuses.add(status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return statuses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task find(long id) {
		Task task = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select t.id, t.name, t.date, t.description, t.status as status_id, t.date_added, t.user_added, t.date_modified, t.user_modified, t.deleted as task_deleted, "
				+ "     s.name as status_name, "
				+ "     ua.id as ua_id, ua.email as ua_email, ua.phone as ua_phone, ua.first_name as ua_first_name, ua.last_name as ua_last_name, ua.facebook as ua_facebook, ua.recognition_opt_in as ua_recognition_opt_in, "
				+ "     um.id as um_id, um.email as um_email, um.phone as um_phone, um.first_name as um_first_name, um.last_name as um_last_name, um.facebook as um_facebook, um.recognition_opt_in as um_recognition_opt_in "
				+ " from task_tracker.task t "
				+ "      left join task_tracker.task_status s on t.status = s.id "
				+ "      left join task_tracker.user ua on t.user_added = ua.id "
				+ "      left join task_tracker.user um on t.user_modified = um.id "
				+ " where t.id = ? ");
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				task = new Task();
				task.setId(resultSet.getLong("id"));
				task.setName(resultSet.getString("name"));
				task.setDate(resultSet.getDate("date"));
				task.setDescription(resultSet.getString("description"));
				task.setStatus(new TaskStatus());
				task.getStatus().setId(resultSet.getLong("status_id"));
				task.getStatus().setName(resultSet.getString("status_name"));
				task.setDateAdded(resultSet.getDate("date_added"));
				task.setUserAdded(new User());
				task.getUserAdded().setId(resultSet.getLong("ua_id"));
				task.getUserAdded().setEmail(resultSet.getString("ua_email"));
				task.getUserAdded().setPhone(resultSet.getString("ua_phone"));
				task.getUserAdded().setFirstName(resultSet.getString("ua_first_name"));
				task.getUserAdded().setLastName(resultSet.getString("ua_last_name"));
				task.getUserAdded().setFacebook(resultSet.getString("ua_facebook"));
				task.getUserAdded().setRecognitionOptIn(resultSet.getBoolean("ua_recognition_opt_in"));
				task.setDateModified(resultSet.getDate("date_modified"));
				task.setUserModified(new User());
				task.getUserModified().setId(resultSet.getLong("um_id"));
				task.getUserModified().setEmail(resultSet.getString("um_email"));
				task.getUserModified().setPhone(resultSet.getString("um_phone"));
				task.getUserModified().setFirstName(resultSet.getString("um_first_name"));
				task.getUserModified().setLastName(resultSet.getString("um_last_name"));
				task.getUserModified().setFacebook(resultSet.getString("um_facebook"));
				task.getUserModified().setRecognitionOptIn(resultSet.getBoolean("um_recognition_opt_in"));
				task.setDeleted(resultSet.getBoolean("task_deleted"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		task.setCategories(this.findCategories(id));
		task.setUsers(this.findUsers(id));
		task.setPartners(this.findPartners(id));
			
		return task;
	}
	
	/**
	 * Finds task categories.
	 * 
	 * @param taskId the task id
	 * @return the task categories
	 */
	private List<Category> findCategories(long taskId) {
		List<Category> categories = new ArrayList<Category>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select c.id, c.name "
				+ " from task_tracker.category c "
				+ "      join task_tracker.task_category tc on c.id = tc.category_id "
				+ " where tc.task_id = ? ");
			preparedStatement.setLong(1, taskId);
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
	 * Finds task users.
	 * 
	 * @param taskId the task id
	 * @return the task users
	 */
	private List<TaskUser> findUsers(long taskId) {
		List<TaskUser> users = new ArrayList<TaskUser>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select u.id, u.email, u.phone, u.first_name, u.last_name, u.facebook, u.recognition_opt_in, tu.status_id "
				+ " from task_tracker.user u "
				+ "      join task_tracker.task_user tu on u.id = tu.user_id "
				+ " where tu.task_id = ? ");
			preparedStatement.setLong(1, taskId);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				TaskUser taskUser = new TaskUser();
				User user = new User();
				user.setId(resultSet.getLong("id"));
				user.setEmail(resultSet.getString("email"));
				user.setPhone(resultSet.getString("phone"));
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setFacebook(resultSet.getString("facebook"));
				user.setRecognitionOptIn(resultSet.getBoolean("recognition_opt_in"));
				taskUser.setUser(user);
				taskUser.setStatusId(resultSet.getLong("status_id"));
				users.add(taskUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return users;
	}

	/**
	 * Finds task partners.
	 * 
	 * @param taskId the task id
	 * @return the task partners
	 */
	private List<Partner> findPartners(long taskId) {
		List<Partner> partners = new ArrayList<Partner>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"select p.id, p.email, p.phone, p.name, p.contact_first_name, p.contact_last_name, p.facebook, p.recognition_opt_in "
				+ " from task_tracker.partner p "
				+ "      join task_tracker.task_partner tp on p.id = tp.partner_id "
				+ " where tp.task_id = ? ");
			preparedStatement.setLong(1, taskId);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Partner partner = new Partner();
				partner.setId(resultSet.getLong("id"));
				partner.setEmail(resultSet.getString("email"));
				partner.setPhone(resultSet.getString("phone"));
				partner.setName(resultSet.getString("name"));
				partner.setContactFirstName(resultSet.getString("contact_first_name"));
				partner.setContactLastName(resultSet.getString("contact_last_name"));
				partner.setFacebook(resultSet.getString("facebook"));
				partner.setRecognitionOptIn(resultSet.getBoolean("recognition_opt_in"));
				
				partners.add(partner);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return partners;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task create(Task task, String userEmail) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"insert into task_tracker.task(name, date, description, status, user_added, date_added) values(?, ?, ?, ?, (select id from task_tracker.user where email = ?), current_timestamp)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, task.getName());
			if (task.getDate() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(task.getDate());
				calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
				preparedStatement.setDate(2, new Date(task.getDate().getTime()), calendar);
			} else {
				preparedStatement.setNull(2, Types.DATE);
			}
			preparedStatement.setString(3, task.getDescription());
			preparedStatement.setLong(4, task.getStatus().getId());
			preparedStatement.setString(5, userEmail);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (resultSet.next()) {
				task.setId(resultSet.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		for (Category category : task.getCategories()) {
			this.addCategory(task.getId(), category.getId());
		}
		
//		for (TaskUser user : task.getUsers()) {
//			this.addUser(task.getId(), user.getUser().getId(), user.getStatusId());
//		}
		
//		for (Partner partner : task.getPartners()) {
//			this.addPartner(task.getId(), partner.getId());
//		}

		return task;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task update(Task task, String userEmail) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"update task_tracker.task set name = ?, date = ?, description = ?, status = ?, user_modified = (select id from task_tracker.user where email = ?), date_modified = current_timestamp where id = ? ");
			preparedStatement.setString(1, task.getName());
			if (task.getDate() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(task.getDate());
				calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
				preparedStatement.setDate(2, new Date(task.getDate().getTime()), calendar);
			} else {
				preparedStatement.setNull(2, Types.DATE);
			}
			preparedStatement.setString(3, task.getDescription());
			preparedStatement.setLong(4, task.getStatus().getId());
			preparedStatement.setString(5, userEmail);
			preparedStatement.setLong(6, task.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		this.deleteCategories(task.getId());
		for (Category category : task.getCategories()) {
			this.addCategory(task.getId(), category.getId());
		}
		
//		this.deleteUsers(task.getId());
//		for (TaskUser user : task.getUsers()) {
//			this.addUser(task.getId(), user.getUser().getId(), user.getStatusId());
//		}
		
//		this.deletePartners(task.getId());
//		for (Partner partner : task.getPartners()) {
//			this.addPartner(task.getId(), partner.getId());
//		}
		
		return task;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task delete(long taskId, String userEmail) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"update task_tracker.task set deleted = true, user_modified = (select id from task_tracker.user where email = ?), date_modified = current_timestamp where id = ? ");
			preparedStatement.setString(1, userEmail);
			preparedStatement.setLong(2, taskId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
		
		return this.find(taskId);
	}
	
	/**
	 * Removes a user from a task.
	 * 
	 * @param taskId the task id
	 * @param userId the user id
	 */
	@Override
	public void removeUser(long taskId, long userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"delete from task_tracker.task_user where task_id = ? and user_id = ? ");
			preparedStatement.setLong(1, taskId);
			preparedStatement.setLong(2, userId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * Adds a user to a task.
	 * 
	 * @param taskId the task id
	 * @param userId the user id
	 * @param statusId the task user status id
	 */
	@Override
	public void addUser(long taskId, long userId, long statusId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"insert into task_tracker.task_user(task_id, user_id, status_id) values (?, ?, ?)");
			preparedStatement.setLong(1, taskId);
			preparedStatement.setLong(2, userId);
			preparedStatement.setLong(3, statusId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * Deletes all categories from a task.
	 * 
	 * @param taskId the task id
	 */
	private void deleteCategories(long taskId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"delete from task_tracker.task_category where task_id = ? ");
			preparedStatement.setLong(1, taskId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}		
	}

	/**
	 * Adds a category to a task.
	 * 
	 * @param taskId the task id
	 * @param categoryId the category id
	 */
	private void addCategory(long taskId, long categoryId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"insert into task_tracker.task_category(task_id, category_id) values (?, ?)");
			preparedStatement.setLong(1, taskId);
			preparedStatement.setLong(2, categoryId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * Deletes all partners from a task.
	 * 
	 * @param taskId the task id
	 */
	private void deletePartners(long taskId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"delete from task_tracker.task_partner where task_id = ? ");
			preparedStatement.setLong(1, taskId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * Adds a partner to a task.
	 * 
	 * @param taskId the task id
	 * @param partnerId the partner id
	 */
	private void addPartner(long taskId, long partnerId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = this.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(
				"insert into task_tracker.task_partner(task_id, partner_id) values (?, ?)");
			preparedStatement.setLong(1, taskId);
			preparedStatement.setLong(2, partnerId);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(connection, preparedStatement, resultSet);
		}
	}
}
