package edu.kingsbury.task_tracker.task.comment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import edu.kingsbury.task_tracker.Feedback;
import edu.kingsbury.task_tracker.task.Task;
import edu.kingsbury.task_tracker.task.TaskDao;
import edu.kingsbury.task_tracker.task.TaskPostgresDao;
import edu.kingsbury.task_tracker.task.TaskUser;
import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;

/**
 * Web service for {@link Comment}.
 * 
 * @author brian
 */
@Path("/tasks/{taskId}/comments")
public class CommentService {

	/**
	 * The comment dao.
	 */
	private CommentDao commentDao;
	
	/**
	 * The comment validator.
	 */
	private CommentValidator commentValidator;
	
	/**
	 * The task dao.
	 */
	private TaskDao taskDao;
	
	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * Constructor initializes fields.
	 */
	public CommentService() {
		this.commentDao = new CommentPostgresDao();
		this.commentValidator = new CommentValidator();
		this.userDao = new UserPostgresDao();
		this.taskDao = new TaskPostgresDao();
	}
	
	/**
	 * Adds a comment.
	 * 
	 * @param comment the comment
	 * @param request the request
	 * @return the added comment
	 * @throws EmailException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Path("/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(
		Comment comment,
		@Context HttpServletRequest request) throws EmailException, FileNotFoundException, IOException {
		
		comment.setUser(this.userDao.find(request.getUserPrincipal().getName()));
		Feedback feedback = this.commentValidator.validate(comment);
		
		if (feedback.isValid()) {
			Comment savedComment = this.commentDao.add(comment);
			Task task = this.taskDao.find(comment.getTaskId(), request.isUserInRole("admin"));
			List<Comment> comments = this.commentDao.findComments(task.getId());
			User loggedOnUser = this.userDao.find(request.getUserPrincipal().getName());
			Set<String> recipients = new TreeSet<String>();
			recipients.add(task.getUserAdded().getEmail());
			for (TaskUser user : task.getUsers()) {
				if (loggedOnUser.getId() != user.getUser().getId()) {
					recipients.add(user.getUser().getEmail());
				}
			}
			for (Comment tempComment : comments) {
				if (loggedOnUser.getId() != tempComment.getUser().getId()) {
					recipients.add(tempComment.getUser().getEmail());
				}
			}
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(System.getProperty("catalina.home") + File.separator + "/conf/kvn.properties"));
			
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(465);
			email.setAuthentication(properties.getProperty("email"), properties.getProperty("password"));
			email.setSSLOnConnect(true);
			email.setFrom(properties.getProperty("email"));
			email.addTo(loggedOnUser.getEmail());
			email.addBcc(recipients.toArray(new String[] {}));
			email.setSubject("Comment added: " + task.getName());
			StringBuilder message = new StringBuilder();
			message.append("<html><body style='font-family: sans-serif;'>");
			message.append("<p style='margin-bottom: 1em;'>A comment has been added to task: " + task.getName() + "</p>");
			message.append("<p>" + loggedOnUser.getFirstName() + " " + loggedOnUser.getLastName() + " says:");
			message.append("<p>\"" + comment.getText() + "\"</p>");
			message.append("<p style='margin-bottom: 1em;'>To view the comment, click <a href='" + properties.getProperty("taskUrl") + task.getId()  + "'>here</a>.");
			message.append("<p style='margin-bottom: 1em;'>Thanks,</p>");
			message.append("<p style='margin-bottom: 1em;'>Kingsbury Community Volunteer Network</p>");
			message.append("</body></html>");
			email.setMsg(message.toString());
			
			email.send();
			
			return Response
				.status(Response.Status.OK)
				.entity(savedComment)
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
	
	/**
	 * Edits a comment.
	 * 
	 * @param comment the comment
	 * @param request the request
	 * @return the edited comment
	 */
	@Path("/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(
		Comment comment,
		@Context HttpServletRequest request) {
		
		comment.setUser(this.userDao.find(request.getUserPrincipal().getName()));
		Feedback feedback = this.commentValidator.validate(comment);
		
		if (feedback.isValid()) {
			Comment savedComment = this.commentDao.edit(comment);
			return Response
				.status(Response.Status.OK)
				.entity(savedComment)
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
	
	/**
	 * Deletes a comment.
	 * 
	 * @param id the comment id
	 * @param request the request
	 * @return feedback
	 */
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		User user = this.userDao.find(request.getUserPrincipal().getName());
		Comment comment = this.commentDao.delete(id, user.getId());
		
		return Response
			.status(comment.isDeleted() ? Response.Status.OK : Response.Status.NOT_FOUND)
			.entity(new Feedback())
			.build();
	}
}
