package edu.kingsbury.task_tracker;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

/**
 * Web service for the app.
 * 
 * @author brian
 */
@Path("/")
public class AppService {

	/**
	 * Loads the app view.
	 * @return the app view
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable app() {
		return new Viewable("/app");
	}
}
