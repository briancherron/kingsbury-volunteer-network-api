package edu.kingsbury.task_tracker.invitation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import edu.kingsbury.task_tracker.Feedback;
import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;

/**
 * Web service for {@link Invitation}.
 * 
 * @author brian
 */
@Path("/invitations")
public class InvitationService {
	
	/**
	 * The invitation validator.
	 */
	private InvitationValidator invitationValidator;
	
	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * Constructor initializes fields.
	 */
	public InvitationService() {
		this.invitationValidator = new InvitationValidator();
		this.userDao = new UserPostgresDao();
	}
	
	@Path("/{invitationKey}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findInvitation(
		@PathParam("invitationKey") UUID invitationKey) {
		
		User user = this.userDao.find(invitationKey);
		if (user != null) {
			return Response
				.status(Response.Status.OK)
				.entity(user)
				.build();
		} else {
			return Response
				.status(Response.Status.OK)
				.entity(user)
				.build();
		}
	}

	/**
	 * Sends an invitation.
	 * 
	 * @param invitation the invitation
	 * @return the response
	 */
	@Path("/send")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendInvitation(Invitation invitation)  throws FileNotFoundException, IOException, EmailException{
		Feedback feedback = this.invitationValidator.validateInvitation(invitation);
		
		if (feedback.isValid()) {
				User user = this.userDao.find(invitation.getEmail());
				if (user == null) {
					user = new User();
					user.setEmail(invitation.getEmail());
					user.setFirstName(invitation.getFirstName());
					user.setLastName(invitation.getLastName());
					user = this.userDao.create(user);
				}
				
				Properties properties = new Properties();
				properties.load(new FileInputStream(System.getProperty("catalina.home") + File.separator + "/conf/kvn.properties"));
				
				HtmlEmail email = new HtmlEmail();
				email.setHostName("smtp.gmail.com");
				email.setSmtpPort(465);
				email.setAuthentication(properties.getProperty("email"), properties.getProperty("password"));
				email.setSSLOnConnect(true);
				email.setFrom(properties.getProperty("email"));
				email.addTo(invitation.getEmail());
				email.setSubject("You're invited to join the Kingsbury Community Volunteer Network!");
				StringBuilder message = new StringBuilder();
				message.append("<html><body>");
				message.append("<p>" + invitation.getFirstName() + ",</p>");
				message.append("<p>You've been invited to join the Kingsbury Community Volunteer Network.<br><br>");
				message.append("<p>To join, click <a href='" + properties.getProperty("joinUrl") + user.getInvitationKey() + "'>here</a>.<br><br>");
				message.append("<p>Thanks,<br>Kingsbury Community Volunteer Network</p>");
				message.append("</body></html>");
				email.setMsg(message.toString());
				
				email.send();
			return Response
				.status(Response.Status.OK)
				.entity(invitation)
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
}
