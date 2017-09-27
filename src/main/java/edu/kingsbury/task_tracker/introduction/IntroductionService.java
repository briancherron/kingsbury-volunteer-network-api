package edu.kingsbury.task_tracker.introduction;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;

/**
 * Web service for the app introduction.
 * 
 * @author brian
 */
@Path("/introduction")
public class IntroductionService {
	
	/**
	 * The introduction dao.
	 */
	private IntroductionDao introductionDao;
	
	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * Constructor initializes fields.
	 */
	public IntroductionService() {
		this.introductionDao = new IntroductionPostgresDao();
		this.userDao = new UserPostgresDao();
	}

	/**
	 * Finds the introduction text.
	 * 
	 * @return the introduction text
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response findIntroduction() {
		return Response
			.status(Response.Status.OK)
			.entity(this.introductionDao.findIntroduction())
			.build();
	}
	
	/**
	 * Saves the introduction text.
	 * 
	 * @param introduction the introduction
	 * @param request the request
	 * @return the response
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveIntroduction(String introduction, @Context HttpServletRequest request) {
		
		User user = this.userDao.find(request.getUserPrincipal().getName());
		if (!user.isAuthorizedToChangeIntroduction()) {
			return Response
				.status(Response.Status.FORBIDDEN)
				.entity(null)
				.build();
		} else {
			this.introductionDao.saveIntroduction(introduction);
			return Response
				.status(Response.Status.OK)
				.entity(null)
				.build(); 
		}
	}
}
