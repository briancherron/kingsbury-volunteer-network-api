package edu.kingsbury.task_tracker.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import edu.kingsbury.task_tracker.Feedback;
import edu.kingsbury.task_tracker.category.Category;
import edu.kingsbury.task_tracker.task.comment.CommentDao;
import edu.kingsbury.task_tracker.task.comment.CommentPostgresDao;
import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;

/**
 * Web service for {@link Task}.
 * 
 * @author brian
 */
@Path("/tasks")
public class TaskService {
	
	/**
	 * The comment dao.
	 */
	private CommentDao commentDao;
	
	/**
	 * The task dao.
	 */
	private TaskDao taskDao;
	
	/**
	 * The task validator.
	 */
	private TaskValidator taskValidator;
	
	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * Constructor initializes the dao.
	 */
	public TaskService() {
		this.commentDao = new CommentPostgresDao();
		this.taskDao = new TaskPostgresDao();
		this.taskValidator = new TaskValidator();
		this.userDao = new UserPostgresDao();
	}

	/**
	 * Finds all tasks.
	 * 
	 * @return all tasks
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(
		@BeanParam Filter filter, 
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.find(filter, request.isUserInRole("admin")))
			.build();
	}
	
	@GET
	@Path("/statuses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStatuses() {
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.findAllStatuses())
			.build();
	}
	
	@GET
	@Path("/audiences")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAudiences() {
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.findAllAudiences())
			.build();
	}
	
	/**
	 * Finds a task.
	 * 
	 * @param id the task id
	 * @return the matching task
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		Status status = Response.Status.OK;
		Task task = this.taskDao.find(id, request.isUserInRole("admin"));
		if (task == null) {
			status = Response.Status.NOT_FOUND;
		} else {
			task.setComments(this.commentDao.findComments(task.getId()));
		}
		
		return Response
			.status(status)
			.entity(task)
			.build();
	}
	
	/**
	 * Creates a task.
	 * 
	 * @param task the task to create
	 * @param request the request
	 * @return the created task
	 * @throws EmailException 
	 * @throws AddressException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		Task task,
		@Context HttpServletRequest request) throws EmailException, AddressException, FileNotFoundException, IOException {
		
		Feedback feedback = this.taskValidator.validate(task);
		if (feedback.isValid()) {
			Task createdTask = this.taskDao.create(task, request.getUserPrincipal().getName());
			
			return Response
				.status(Response.Status.OK)
				.entity(createdTask)
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
	
	/**
	 * Adds the signed on user to a task.
	 * 
	 * @param id the task id
	 * @param request the request
	 * @return blank feedback
	 */
	@Path("/{id}/add-me")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMe(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		User user = this.userDao.find(request.getUserPrincipal().getName());
		this.taskDao.addUser(id, user.getId(), 2);
		
		return Response
			.status(Response.Status.OK)
			.entity(new Feedback())
			.build();
	}
	
	/**
	 * Removes the signed on user from a task.
	 * 
	 * @param id the task id
	 * @param request the request
	 * @return blank feedback
	 */
	@Path("/{id}/remove-me")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMe(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		User user = this.userDao.find(request.getUserPrincipal().getName());
		this.taskDao.removeUser(id, user.getId());
		
		return Response
			.status(Response.Status.OK)
			.entity(new Feedback())
			.build();
		
	}
	
	/**
	 * Sends invitations for a task.
	 * 
	 * @param id the task id
	 * @param invitees the users to send invitations to
	 * @return empty feedback
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws EmailException
	 */
	@POST
	@Path("/{id}/send-invitations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendInvitations(
		@PathParam("id") long id, 
		TaskInvitation taskInvitation,
		@Context HttpServletRequest request) throws FileNotFoundException, IOException, EmailException {
		
		Task task = this.taskDao.find(id, request.isUserInRole("admin"));
		User loggedOnUser = this.userDao.find(request.getUserPrincipal().getName());
		for (TaskUser user : taskInvitation.getInvitees()) {
			if (user.getStatusId() == 1) {
				this.taskDao.addUser(id, user.getUser().getId(), user.getStatusId());
				
				Properties properties = new Properties();
				properties.load(new FileInputStream(System.getProperty("catalina.home") + File.separator + "/conf/kvn.properties"));
				
				HtmlEmail email = new HtmlEmail();
				email.setHostName("smtp.gmail.com");
				email.setSmtpPort(465);
				email.setAuthentication(properties.getProperty("email"), properties.getProperty("password"));
				email.setSSLOnConnect(true);
				email.setFrom(properties.getProperty("email"));
				email.addTo(user.getUser().getEmail());
				email.setSubject("You're invited: " + task.getName());
				StringBuilder message = new StringBuilder();
				message.append("<html><body style='font-family: sans-serif;'>");
				message.append("<p style='margin-bottom: 1em;'>" + user.getUser().getFirstName() + ",</p>");
				message.append("<p style='margin-bottom: 1em;'>You've been invited to volunteer for the following task:");
				message.append("<p style='margin-bottom: 1em;'>" + task.getName() + "</p>");
				if (StringUtils.isNotBlank(taskInvitation.getMessage())) {
					message.append("<p>" + loggedOnUser.getFirstName() + " " + loggedOnUser.getLastName() + " says:");
					message.append("<p>\"" + taskInvitation.getMessage() + "\"</p>");
				}
				message.append("<p style='margin-bottom: 1em;'>To participate, click <a href='" + properties.getProperty("taskUrl") + task.getId()  + "'>here</a>.");
				message.append("<p style='margin-bottom: 1em;'>Thanks,</p>");
				message.append("<p style='margin-bottom: 1em;'>Kingsbury Community Volunteer Network</p>");
				message.append("</body></html>");
				email.setMsg(message.toString());
				
				email.send();
			}
		}
		
		return Response
			.status(Response.Status.OK)
			.entity(new Feedback())
			.build();
	}
	
	/**
	 * Updates the interests or skills for a task.
	 * 
	 * @param id the task id
	 * @param categories the categories 
	 * @return blank feedback
	 */
	@Path("/{id}/interests-or-skills")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInterestsOrSkills(
		@PathParam("id") long id,
		List<Category> categories) {
		
		this.taskDao.removeCategories(id);
		this.taskDao.addCategories(id, categories);
		
		return Response
			.status(Response.Status.OK)
			.entity(new Feedback())
			.build();
	}
	
	/**
	 * Updates a task.
	 * 
	 * @param task the task to update
	 * @param id the task id
	 * @param request the request
	 * @return the updated task
	 * @throws EmailException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(
		Task task,
		@PathParam("id") long id,
		@Context HttpServletRequest request) throws FileNotFoundException, IOException, EmailException {
		
		Feedback feedback = this.taskValidator.validate(task);
		if (feedback.isValid()) {
			Task updatedTask = this.taskDao.update(task, request.getUserPrincipal().getName());
			
			return Response
				.status(Response.Status.OK)
				.entity(updatedTask)
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
	
	/**
	 * Deletes a task.
	 * 
	 * @param id the task id
	 * @param request the request
	 * @return whether the task was deleted
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		Task task = this.taskDao.delete(id, request.getUserPrincipal().getName(), request.isUserInRole("admin"));
		return Response
			.status(task.isDeleted() ? Response.Status.OK : Response.Status.NOT_FOUND)
			.entity(task)
			.build();
	}
}
